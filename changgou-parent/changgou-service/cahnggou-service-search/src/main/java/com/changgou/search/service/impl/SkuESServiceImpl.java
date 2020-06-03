package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.search.dao.SkuESMapper;
import com.changgou.goods.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuESService;
import com.github.pagehelper.PageInfo;
import io.seata.spring.annotation.GlobalTransactional;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Transactional
public class SkuESServiceImpl implements SkuESService {
    @Autowired//注入ElasticsearchTemplate对象：用于执行搜索：高级搜索
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SkuFeign skuServiceFeign;
    @Autowired
    private SkuESMapper skuESMapper;
    @Override
    public void importData(Integer page, Integer size) {
        Result<PageInfo> pageInfoResult = skuServiceFeign.findPage(page, size);
        PageInfo data = pageInfoResult.getData();
        List<Sku> skuList = data.getList();
        List<SkuInfo> skuInfoList= JSON.parseArray(JSON.toJSONString(skuList),SkuInfo.class);
        for (SkuInfo skuInfo : skuInfoList) {
            Map<String,String> specMap=JSON.parseObject(skuInfo.getSpec(),Map.class);
            skuInfo.setSpecMap(specMap);
        }
        skuESMapper.saveAll(skuInfoList);
    }
    @Override
    public Map<String, Object> findSearchSkuMap(Map<String,String> map){
        //根据条件构建对象查询出构建好查询条件的nativeSearchBuilder对象集合
        NativeSearchQueryBuilder nativeSearchQueryBuilder=buildBasicQuery(map);
        //根据nativeSearchQueryBuilder对象，调用searchMap方法查询出根据构造条件查询并封装好的对象
        Map<String, Object> resultMap = searchMap(nativeSearchQueryBuilder);
        //查询对应的参数集合，返回参数列表
        Map<String, Set<String>> specMap = searchSpecMap(nativeSearchQueryBuilder);
        resultMap.put("specMap",specMap);
        //如果用户没传入分类信息，那么我们展示所有分类，但是如果用户选择了分类，那我们就没有在返回分类信息的必要了，所以不进行查询
        if(map.get("category")==null||StringUtils.isEmpty(map.get("category"))){
            resultMap.put("categoryList",searchCategoryList(nativeSearchQueryBuilder));
        }
        //如果用户没传入品牌信息，那么我们展示所有品牌，但是如果用户选择了品牌，那我们就没有在返回品牌信息的必要了，所以不进行查询
        if(map.get("brand")==null||StringUtils.isEmpty(map.get("brand"))){
            resultMap.put("brandList",searchBrandList(nativeSearchQueryBuilder));
        }
        return resultMap;

    }

    /**
     * 将对象传入的搜索map条件集合进行条件的构建，并返回构建好的条件构建器对象
     * @param map
     * @return
     */
    public NativeSearchQueryBuilder buildBasicQuery(Map<String,String> map){
        //获取查询条件创建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //创建组合查询对象BoolQueryBuild
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        //编辑查询条件，按名称搜索产品
        if(map!=null&&map.size()!=0){
            String name = map.get("keywords");
            String categoryName = map.get("category");
            String brandName = map.get("brand");
            //如果用户擦混入的参数为空，则不进行查询
            if(!StringUtils.isEmpty(name)){
                /**
                 * 参数不为空，构建查询条件：
                 * nativeSearchQueryBuilder.withQuery(QueryBuilder queryBuilder):构建查询条件,QueryBuilder对象作为查询的条件参数
                 *QueryBuilders.queryStringQuery(name)：因为QueryBuilder这个对象可以使用QueryBuilders创建，并且可以封装查询条件进去
                 *                                      这个方法是查询一个字符串，我们将参数传递进去就对查询对象封装了查询的参数
                 *QueryBuilders.field("name"):这个方法是要查询哪个域，我们传入参数name他将会在name域中查询我们传入的参数
                 */
                boolQueryBuilder.must(QueryBuilders.queryStringQuery(name).field("name"));
            }
//            else if(!StringUtils.isEmpty(categoryName)) {
//                nativeSearchQueryBuilder.withQuery(QueryBuilders.termQuery("categoryName",categoryName));
//            }else if(!StringUtils.isEmpty(brandName)) {
//                nativeSearchQueryBuilder.withQuery(QueryBuilders.termQuery("brandName",brandName));
//            }
            //使用组合条件条件查询对象BoolQueryBuilder添加查询categoryName和brandName
            if(!StringUtils.isEmpty(categoryName)){
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName",categoryName));
            }
            if(!StringUtils.isEmpty(brandName)){
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName",brandName));
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.startsWith("spec_")){
                    value=value.replace("\\","");
                    boolQueryBuilder.must(QueryBuilders.termQuery("specMap."+key.substring(5)+".keyword",value));
                }
            }
            //根据用户传入的价格区间进行查询
            if(!StringUtils.isEmpty(map.get("price"))){
                String price = map.get("price");
                price=price.replace("元","").replace("以上","");
                String[] prices = price.split("-");
                if(prices!=null||prices.length>0){
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(prices[0]));
                    if(prices.length==2){
                        boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(prices[1]));
                    }
                }

            }
        }
        //根据用户传的参数进行排序
        //在哪个域进行搜索
        String sortField = map.get("sortField");
        //排序规则  DESC：降序  ASC：升序
        String sortRule = map.get("sortRule");
        if(sortField!=null&&!StringUtils.isEmpty(sortField)){
            if(sortRule!=null&&!StringUtils.isEmpty(sortRule)){
                //新品：只进行降序排序
                if("creteDate".equalsIgnoreCase(sortField)){
                    nativeSearchQueryBuilder.withSort(new FieldSortBuilder(sortField).order(SortOrder.valueOf(sortRule)));
                    //按评论数进行排序
                }else if("price".equalsIgnoreCase(sortField)){
                    nativeSearchQueryBuilder.withSort(new FieldSortBuilder(sortField).order(SortOrder.valueOf(sortRule)));
                }
                //使用用户的规则
                nativeSearchQueryBuilder.withSort(new FieldSortBuilder(sortField).order(SortOrder.valueOf(sortRule)));
            }else{
                //用户没传入规则，默认降序
                nativeSearchQueryBuilder.withSort(new FieldSortBuilder(sortField).order(SortOrder.DESC));
            }
        }
        //给数据进行分页
        Integer pageIndex=getPageIndex(map);
        Integer size=20;
        //nativeSearchQueryBuilder.withPageable()分页方法,要一个实现Pageable分页接口的对象构建分页条件，PageRequest分页对象Pageable的实现类
        //调用of方法进行分页，第一个参数为第几页（这里第一页为0页），-1表示将用户传的真实数据-1在进行查询分页结果才正确，第二个参数分页长度哦我们后端写死
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageIndex-1,size));

        //将组合查询对象查询条件整合给 NativeSearchQueryBuilder
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        return nativeSearchQueryBuilder;
    }

    /**
     * 获取前端的page页码
     * @param map
     * @return
     */
    private Integer getPageIndex(Map<String, String> map) {
        String page = map.get("page");
        if(page==null||StringUtils.isEmpty(page)){
            return 1;
        }else {
            try {
                int pageNum=Integer.valueOf(page);
                if(pageNum<1){
                    return 1;
                }
                return pageNum;
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }


    /**
     * 根据构建好条件的构建器对象nativeSearchQueryBuilder查询出符合条件的商品对象、总条数和总记录数并封装到resultMap集合中返回
     * @return      返回的封装好记录数、总条数、sku对象的map集合
     */
    public Map<String,Object> searchMap(NativeSearchQueryBuilder nativeSearchQueryBuilder){
        //创建查询条件
//        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        //进行高亮域配置
        //获取高亮域的类对象HighlightBuilder.Field
        HighlightBuilder.Field field=new HighlightBuilder.Field("name");
        //设置前缀
        field.preTags("<em style='color:red'>");
        //设置后缀
        field.postTags("</em>");
        //设置碎片长度
        field.fragmentSize(100);
        //将高亮域数据给到nativeSearchQueryBuilder对象，通过他的.withHighlightFields(Field..)方法
        nativeSearchQueryBuilder.withHighlightFields(field);
        //查询返回AggregatedPage对象，封装了结果集
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class,
                new SearchResultMapper() {//对返回的数据进行高亮设置
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                        //装高亮数据的集合
                        List<T> infoList=new ArrayList<>();
                        for (SearchHit hit : searchResponse.getHits()) {
                            //获取非高亮数据
                            SkuInfo skuInfo=JSON.parseObject(hit.getSourceAsString(),SkuInfo.class);
                            //分析高亮数据，获取高亮数据,在name域中（包含高亮数据并不是只有高亮数据）
                            HighlightField highlightName = hit.getHighlightFields().get("name");
                            //如果有高亮数据的话，获取高亮数据中的值
                            if(highlightName!=null&&highlightName.getFragments()!=null){
                                //获取所有的高亮数据，可能不止一个。遍历数据并拿到他
                                Text[] fragments = highlightName.getFragments();
                                StringBuilder stringBuilder=new StringBuilder();
                                if(fragments!=null&&fragments.length!=0){
                                    for (Text fragment : fragments) {
                                        stringBuilder.append(fragment.toString());
                                    }
                                }
                                //将高亮数据设置到skuInfo对象的name，因为搜索条件来自于name
                                skuInfo.setName(stringBuilder.toString());
                            }
                            infoList.add((T) skuInfo);
                        }
                        //第一个参数：添加好高亮的集合数据
                        //第二个数据：分页对象pageable，参数传进来的
                        //第三个对象：总条数：SearchResponse可以取出来   searchResponse.getHits().getTotalHits()
                        return new AggregatedPageImpl<T>(infoList,pageable,searchResponse.getHits().getTotalHits());
                    }
                });
        //获取查询的分页---总记录数
        long totalElements = aggregatedPage.getTotalElements();
        //获取查询的分页---总页数
        int totalPages = aggregatedPage.getTotalPages();
        //将sku对象取出来
        List<SkuInfo> skuInfoList = aggregatedPage.getContent();
        //将他们封装到Map集合中并返回
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("rows",skuInfoList);
        resultMap.put("totalElements",totalElements);
        resultMap.put("totalPage",totalPages);
        //获取当前页
        NativeSearchQuery build = nativeSearchQueryBuilder.build();
        Pageable pageable = build.getPageable();
        int pageNumber = pageable.getPageNumber();
        //获取每页显示条数
        int pageSize = pageable.getPageSize();
        resultMap.put("pageNumber",pageNumber);
        resultMap.put("pageSize",pageSize);
        return resultMap;
    }
    public Map<String, Set<String>> searchSpecMap(NativeSearchQueryBuilder nativeSearchQueryBuilder){
        //添加构建条件参数，查询1000条数据，从spec.keyword域中查(“.keyword”表示不进行分词，直接分组查询出来)
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("specStr").field("spec.keyword").size(1000));
        //调用条件构造器进行查询，
        AggregatedPage<SkuInfo> skuInfoAggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);
        //拿到查询的分组结果
        StringTerms stringTerms = skuInfoAggregatedPage.getAggregations().get("specStr");
        //拿到分组的结果的list集合
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        //创建接收参数Map集合，key存属性，如“屏幕尺寸、颜色”，value set集合存属性值“5.0、5.2    黄、红等”
        Map<String,Set<String>> specMap=new HashMap<String,Set<String>>();
        //遍历结果集合
        for (StringTerms.Bucket bucket:buckets){
            //将结果json字符串解析成map集合，key为属性，value为sku本属性的属性值
            Map<String,String> map = JSON.parseObject(bucket.getKeyAsString(), Map.class);
            //遍历map集合
            for (Map.Entry<String, String> entry : map.entrySet()) {
                //拿到key和value
                String key = entry.getKey();
                String value = entry.getValue();
                //由于同一个属性只能存在一个，同一个key的名称也只能存在一个，所以获取这个属性的set属性值集合
                Set<String> specSet = specMap.get(entry.getKey());
                //判断集合是否为空
                if(specSet==null){
                    //不为空，表示当前没有这个属性存在，创建set集合，添加参数属性值参数，拿到返回的Map集合，设置属性为key，value为这个set集合
                    specSet = new HashSet<String>();
                    specSet.add(value);
                    specMap.put(key,specSet);
                }else{
                    //存在，直接将属性值添加到属性的set集合中
                    specSet.add(value);
                }
            }
        }
        return specMap;
    }
    /**
     * 分组查询品牌集合
     * @param nativeSearchQueryBuilder
     * @return
     */
    public List<String> searchBrandList(NativeSearchQueryBuilder nativeSearchQueryBuilder){
        //构建分组条件
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrands").field("brandName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuBrands");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        List<String> brandList=new ArrayList<>();
        for (StringTerms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
            brandList.add(keyAsString);
        }
        return brandList;
    }

    /**
     * 构建分组方法，根据nativeSearchQueryBuilder构建categoryName查询分组条件，并返回categoryList分类名称集合对象
     * @param nativeSearchQueryBuilder
     * @return
     */
    public List<String> searchCategoryList(NativeSearchQueryBuilder nativeSearchQueryBuilder){
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategoryNames").field("categoryName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuCategoryNames");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        List<String> categoryList=new ArrayList<>();
        for (StringTerms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
            categoryList.add(keyAsString);
        }
        return categoryList;
    }
    /**
     * 添加分组查询条件，根据分类名称categoryName进行分组
     * AggregationBuilders.terms("skuCategoryName"):构建分组对象AggregationBuilders，terms方法是给分组起个别名skuCategoryName
     * .field("categoryName")：在哪个域对象进行查询
     * 下面进行了方法抽取
     */
//        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategoryName").field("categoryName"));
//        /**
//         * 进行分组查询，拿到查询条件
//         */
//        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);
//        // kuInfos.getAggregations()拿到分组的对象集合，分组的条件可以不止一个，所以拿到的分组集合也不只一个
//        //.get("skuCategoryName")获取分组名称是skuCategoryName，就是我们给这个分组对象的别名
//        // 这个对象的返回值是一个Aggregation分组接口，我们可以多态拿到他的子类对象StringTerm
//        StringTerms skuCategoryName = skuInfos.getAggregations().get("skuCategoryName");
//        //拿到这个分组对象，他有可能不止一个数据，也就是说可能不只有一组
//        List<StringTerms.Bucket> buckets = skuCategoryName.getBuckets();
//        //遍历这个集合，拿到所有的组名称存放到list集合
//        List<String> categoryList=new ArrayList<>();
//        for (StringTerms.Bucket bucket : buckets) {
//            String keyAsString = bucket.getKeyAsString();
//            categoryList.add(keyAsString);
//        }
}

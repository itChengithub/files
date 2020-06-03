package com.changgou.goods.feign;

import com.changgou.goods.entity.Result;
import com.changgou.goods.pojo.Sku;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "goods")
@RequestMapping("sku")
public interface SkuFeign {
    @GetMapping("/{skuId}/num/{num}")
    public Result<Integer> updateSkuNum(@PathVariable("skuId") Long skuId, @PathVariable("num") Integer num);
    /***
     * Sku分页条件搜索实现
     * @param sku
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false) Sku sku, @PathVariable  int page, @PathVariable  int size);


    /***
     * Sku分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size);

    /***
     * 多条件搜索品牌数据
     * @param sku
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Sku>> findList(@RequestBody(required = false)  Sku sku);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id);

    /***
     * 修改Sku数据
     * @param sku
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  Sku sku,@PathVariable Long id);

    /***
     * 新增Sku数据
     * @param sku
     * @return
     */
    @PostMapping
    public Result add(@RequestBody   Sku sku);

    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable Long id);

    /***
     * 查询Sku全部数据
     * @return
     */
    @GetMapping
    Result<List<Sku>> findAll();

    /**
     * 添加elasticSearch分页查询
     * @return
     */
    @PostMapping("/search/es/{page}/{size}")
    Result<PageInfo<Sku>> findByLike(@RequestBody Sku sku,@PathVariable("page") Integer page,@PathVariable("size") Integer size);
    /**
     * 根据后台传的map集合对库存数据进行更新
     *
     */
    @GetMapping("reloadNum")
    Result reloadNum(@RequestParam Map<String,Integer> map);
}

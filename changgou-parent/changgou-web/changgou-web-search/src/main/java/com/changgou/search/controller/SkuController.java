package com.changgou.search.controller;

import com.changgou.goods.entity.Page;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.feign.SkuFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("search")
public class SkuController {
    @Autowired
    private SkuFeign skuFeign;
    @GetMapping("list")
    public String search(@RequestParam Map<String,String> searchMap, Model model){
        String[] urls = getUrl(searchMap);
        Map<String, Object> resultMap = skuFeign.search(searchMap);
        model.addAttribute("result",resultMap);
        model.addAttribute("searchMap",searchMap);
        //获取当前页
        Integer pageNumber = (Integer) resultMap.get("pageNumber");
        //获取每页显示条数
        Integer pageSize=(Integer) resultMap.get("pageSize") ;
        //获取总记录数
        Page<Object> page = new Page<>(Long.parseLong(resultMap.get("totalElements").toString()),pageNumber,pageSize,4);
        model.addAttribute("page",page);

        model.addAttribute("url",urls[0]);
        model.addAttribute("sortUrl",urls[1]);

        System.out.println(page.getCurrentpage());
        System.out.println(searchMap.get(page));
        return "search";
    }
    public String[] getUrl(Map<String,String> map){
        String url="/search/list";
        String sortUrl="/search/list";
        if(map!=null&&map.size()!=0){
            url+="?";
            sortUrl+="?";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if(entry.getKey().startsWith("spec_")){
                    String value=entry.getValue().replace("%2B","+");
                    map.put(entry.getKey(),value);
                }
                if("page".equalsIgnoreCase(entry.getKey())){
                    continue;
                }
                url+=entry.getKey()+"="+entry.getValue()+"&";
                if("sortField".equalsIgnoreCase(entry.getKey())||"sortRule".equalsIgnoreCase(entry.getKey())){
                    continue;
                }
                sortUrl+=entry.getKey()+"="+entry.getValue()+"&";
            }
            url=url.substring(0,url.length()-1);
            sortUrl=sortUrl.substring(0,sortUrl.length()-1);
        }
        if(url.contains("&&"))url.replace("&&","&");
        if(sortUrl.contains("&&"))url.replace("&&","&");
        return new String[]{url,sortUrl};
    }
}

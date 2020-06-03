package com.changgou.search.controller;

import com.changgou.goods.entity.Result;
import com.changgou.goods.entity.StatusCode;
import com.changgou.search.service.SkuESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/search")
public class SkuESController {
    @Autowired
    private SkuESService skuESService;
    @GetMapping("/import/{page}/{size}")
    public Result importDataPage(@PathVariable("page")Integer page, @PathVariable("size")Integer size){
        skuESService.importData(page,size);
        return new Result(true, StatusCode.OK,"导入ES成功，导入第:"+page+"   页，导入  "+size+"条");
    }

    /**
     * 获取用户传递参数查询
     * @param map
     * @return
     */
    @GetMapping
    public Map<String,Object> search(@RequestParam(required = false) Map map){
        return skuESService.findSearchSkuMap(map);
    }

}

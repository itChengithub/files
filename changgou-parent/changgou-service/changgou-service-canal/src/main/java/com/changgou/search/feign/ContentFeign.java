package com.changgou.search.feign;

import com.changgou.search.content.pojo.Content;
import com.changgou.goods.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@FeignClient("CONTENT")
public interface ContentFeign {
    @GetMapping("/content/contentCategory/{categoryId}")
    Result<List<Content>> findByCategoryId(@PathVariable("categoryId")Long categoryId);
}

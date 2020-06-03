package com.changgou.user.feign;

import com.changgou.goods.entity.Result;
import com.changgou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("user")
@RequestMapping("/user")
public interface UserFeign {
    @GetMapping("/{id}")
    Result<User> findById(@PathVariable String id);
    @RequestMapping("/points/add")
    Result addPoints(String username);
}

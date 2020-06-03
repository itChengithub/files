package com.changgou.oauth.controller;

import com.changgou.goods.entity.Result;
import com.changgou.goods.entity.StatusCode;
import com.changgou.oauth.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户的登录方法
 */
@RestController
@RequestMapping("/user")
public class UserLoginController {
    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;
    private String grand_type="password";
    @Autowired
    private UserLoginService userLoginService;
    @RequestMapping("login")
    public Result login(String username,String password){
        userLoginService.login(username,password,grand_type,clientId,clientSecret);
        return new Result(true, StatusCode.OK,"登录认证成功");
    }
}

package com.changgou.oauth.service;

public interface UserLoginService {
    void login(String username,String password,String grand_type,String clientId,String clientSecret);
}

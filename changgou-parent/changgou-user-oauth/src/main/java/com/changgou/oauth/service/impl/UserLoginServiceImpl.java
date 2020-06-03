package com.changgou.oauth.service.impl;

import com.changgou.oauth.service.UserLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    private String url="http://localhost:9001/oauth/token";

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public void login(String username, String password, String grand_type, String clientId, String clientSecret) {
        //请求体封装
        MultiValueMap<String,String> bodyMap=new LinkedMultiValueMap<>();
        bodyMap.add("username",username);
        bodyMap.add("password",password);
        bodyMap.add("grand_type","password");
        //封装请求头
        MultiValueMap<String,String> headerMap=new LinkedMultiValueMap<>();
        String authorization="Basic "+ Base64.getEncoder().encode((clientId+":"+clientSecret).getBytes());
        headerMap.add("Authorization",authorization);
        //封装相应实体对象
        HttpEntity httpEntity = new HttpEntity(bodyMap,headerMap);
        System.out.println(httpEntity.getBody());
        //需要三个参数： 1. 请求的路径   2. 请求的方式  3. 请求内容的封装 4. Map集合
        restTemplate.exchange(url, HttpMethod.POST,httpEntity,Map.class);

    }
}

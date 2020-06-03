package com.changgou.test;

import com.changgou.goods.util.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestWXPay {
    @Test
    public void wxPayTest() throws Exception {
        //生成随机字符
        String str = WXPayUtil.generateNonceStr();
        System.out.println(str);
        //将map集合转成xml字符串,创建签名
        Map<String,String> map=new HashMap<>();
        map.put("test","helloWxPay");
        map.put("test","helloWxPay");
        map.put("test","helloWxPay");
        map.put("test","helloWxPay");
        String s = WXPayUtil.generateSignedXml(map,"chen");
        System.out.println(s);
        //将xml字符串转成map集合
        Map<String, String> map1 = WXPayUtil.xmlToMap(s);
        System.out.println(map1);
    }
    @Test
    public void testHttpClient() throws IOException {
        //请求地址
        String url ="https://api.mch.weixin.qq.com/pay/orderquery";
        //创建请求对象
        HttpClient httpClient=new HttpClient(url);
        //设置请求参数，xml格式
        httpClient.setXmlParam("<xml><name>张三</name></xml>");
        //设置协议为https
        httpClient.setHttps(true);
        //发送Post请求：xml参数必须为post请求
        httpClient.post();
        //获取响应数据
        String content = httpClient.getContent();
        System.out.println(content);
    }
}

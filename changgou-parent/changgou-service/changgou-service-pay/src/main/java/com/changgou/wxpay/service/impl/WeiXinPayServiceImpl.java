package com.changgou.wxpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.util.HttpClient;
import com.changgou.wxpay.service.WeiXinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class WeiXinPayServiceImpl implements WeiXinPayService {
    //应用id
    @Value("${weixin.appid}")
    private String appid;
    //商户账号
    @Value("${weixin.partner}")
    private String partner;
    //密钥
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    //回调地址
    @Value("${weixin.notifyurl}")
    private String notifyurl;



    @Override
    public Map<String,String> createPay(Map<String,String> parameterMap) {
        try {
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("appid",appid);
            paramMap.put("mch_id",partner);
            //随机字符串
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            //订单号
            paramMap.put("out_trade_no",parameterMap.get("outtradeno"));
            //商品描述
            paramMap.put("body","购物商城");
            //交易金额：分为单位
            paramMap.put("total_fee",parameterMap.get("price"));
            //终端IP
            paramMap.put("spbill_create_ip","127.0.0.1");
            //通知地址
            paramMap.put("notify_url",notifyurl);
            //交易类型
            paramMap.put("trade_type","NATIVE ");
            //创建routingKey和交换机对象exchange
            String routingKey = parameterMap.get("routingKey");
            String exchange = parameterMap.get("exchange");
            String username = parameterMap.get("username");
            Map<String,String> map=new HashMap<String, String>();
            map.put("routingKey",routingKey);
            map.put("exchange",exchange);
            map.put("username",username);


            String attach = JSON.toJSONString(map);
            paramMap.put("attach",attach);

            String param = WXPayUtil.generateSignedXml(paramMap, partnerkey);
            //创建Http对象
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(param);
            httpClient.post();
            String content = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> queryStatus(String outtradno) {
        try {
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("appid",appid);
            paramMap.put("mch_id",partner);
            //随机字符串
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paramMap.put("out_trade_no", outtradno);

            String param=WXPayUtil.generateSignedXml(paramMap,partnerkey);
            //创建Http对象
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(param);
            httpClient.post();
            String content = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

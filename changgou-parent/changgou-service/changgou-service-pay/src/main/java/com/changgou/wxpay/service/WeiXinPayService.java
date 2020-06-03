package com.changgou.wxpay.service;

import java.util.Map;

public interface WeiXinPayService {
    Map<String,String> createPay(Map<String,String> parameterMap);

    Map<String, String> queryStatus(String outtradno);
}

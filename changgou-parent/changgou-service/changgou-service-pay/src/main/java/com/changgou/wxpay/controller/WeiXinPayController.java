package com.changgou.wxpay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.entity.Result;
import com.changgou.goods.entity.StatusCode;
import com.changgou.wxpay.service.WeiXinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.ibatis.mapping.ResultMap;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/weixin/pay")
public class WeiXinPayController {
    @Autowired
    private WeiXinPayService weiXinPayService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     *
     */
    @RequestMapping("/notify/url")
    public String notify(HttpServletRequest request) throws Exception {
        ServletInputStream is = request.getInputStream();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;
        if((len=is.read(buffer))!=-1){
            baos.write(buffer,0,len);
        }
        byte[] bytes = baos.toByteArray();
        String resultXml = new String(bytes, "UTF-8");
        Map<String, String> map = WXPayUtil.xmlToMap(resultXml);
        //获取交换机和routingKey
        Map<String,String> mqMap = JSON.parseObject(map.get("attach"), Map.class);
        //将信息交给交换机
        rabbitTemplate.convertAndSend(mqMap.get("exchange"),mqMap.get("routingKey"),JSON.toJSONString(map));
        System.out.println(resultXml);
        System.out.println(map);
        String result="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        return result;
    }
    /**
     * 支付查询方法
     * @param parameterMap
     * @return
     */
    @GetMapping("/create/pay")
    public Result<Map<String,String>> createPay(@RequestParam Map<String,String> parameterMap){
        Map<String, String> resultMap = weiXinPayService.createPay(parameterMap);
        return new Result<Map<String,String>>(true, StatusCode.OK,"创建支付二维码成功",resultMap);

    }

    /**
     * 创建支付
     * @param outtradno
     * @return
     */
    @GetMapping("/query/status")
    public Result<Map<String,String>> queryStatus(@RequestParam String outtradno){
        Map<String, String> resultMap = weiXinPayService.queryStatus(outtradno);
        return new Result<Map<String,String>>(true, StatusCode.OK,"查询支付状态成功",resultMap);

    }
}

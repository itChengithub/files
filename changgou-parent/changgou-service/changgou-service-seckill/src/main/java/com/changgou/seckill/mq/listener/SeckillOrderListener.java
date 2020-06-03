package com.changgou.seckill.mq.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 秒杀订单监听
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.seckillorder}")
public class SeckillOrderListener {
    @Autowired
    private SeckillOrderService seckillOrderService;
    @RabbitHandler
    public void listenerTrue(String message){
        System.out.println(message);
        Map<String,String> map = JSON.parseObject(message, Map.class);
        String return_code=map.get("return_code");
        if ("SUCCESS".equals(return_code)){
            //获取业务结果
            String result_code=map.get("result_code");
            String transaction_id=map.get("transaction_id");
//                String successTime=map.get(" time_end");
//                System.out.println(successTime);
            String payTime=map.get("time_end");
            String attach=map.get("attach");
            if("SUCCESS".equals(result_code)){
                //获取订单号,调用业务层实现订单状态的修改
                Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
                String username = attachMap.get("username");
                seckillOrderService.payOk(username,transaction_id,payTime);
            }else {
                //未成功回滚库存
                Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
                String username = attachMap.get("username");
                seckillOrderService.deleteOrder(username);
            }
        }
    }
}

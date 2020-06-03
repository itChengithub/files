package com.changgou.order.mq.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;

@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class PayResultListener {
    @Autowired
    private OrderService orderService;
    @RabbitHandler
    public void payListener(String message) throws ParseException {
        Map<String,String> map= JSON.parseObject(message,Map.class);
        System.out.println("-------------------------------------------------------");
        System.out.println(map);

        //获取通信结果
        String return_code=map.get("return_code");
        if ("SUCCESS".equals(return_code)){
            //获取业务结果
            String result_code=map.get("result_code");
            if("SUCCESS".equals(result_code)){
                //获取订单号,调用业务层实现订单状态的修改
                String transaction_id=map.get("transaction_id");
//                String successTime=map.get(" time_end");
//                System.out.println(successTime);
                String successTimeStr=map.get("time_end");
                String orderId=map.get("out_trade_no");
                orderService.updateStatus(orderId,successTimeStr,transaction_id);
                System.out.println("通信状态:"+return_code+",交易状态:"+result_code+"订单号："+transaction_id+"时间："+successTimeStr);
            }else {
                //未成功回滚库存
                String orderId=map.get("out_trade_no");
                orderService.deleteOrder(orderId);
            }
        }
    }
}

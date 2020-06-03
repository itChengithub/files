package com.changgou.order.mq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@RabbitListener(queues = "listenerQueue")
public class OverTimeListener {
    @RabbitHandler
    public void overTime(String orderId){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("已超时超时时间："+simpleDateFormat.format(new Date()));
        System.out.println("订单Id"+orderId);
    }
}

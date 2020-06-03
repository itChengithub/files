package com.changgou.seckill.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    @Value("${mq.pay.outTime.SeckillKey}")
    private String seckillKey;
    @Value("${mq.pay.outTime.OutTimeQueue}")
    private String outTimeQueue;
    @Value("${mq.pay.outTime.SeckillQueue}")
    private String seckillQueue;
    @Value("${mq.pay.outTime.SeckillExchange}")
    private String seckillExchange;


    @Bean
    public Queue outTimeQueue(){
        return QueueBuilder.durable(outTimeQueue)
                .withArgument("x-dead-letter-exchange",seckillExchange)//死信交换机dExchange将信息转给listenerExchange交换机
                .withArgument("x-dead-letter-routing-key",seckillKey).build();//将路由的key指向监听队列的路由
    }

    @Bean
    public Queue seckillQueue(){
        return new Queue(seckillQueue);
    }

    @Bean
    public Exchange seckillExchange(){
        return new DirectExchange(seckillExchange);
    }

    @Bean
    public Binding seckillBinding(Queue seckillQueue,Exchange seckillExchange){
        return BindingBuilder.bind(seckillQueue).to(seckillExchange).with(seckillKey).noargs();
    }

}

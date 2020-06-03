package com.changgou.wxpay.mq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    //注入交换机名称
    @Value("${mq.pay.exchange.order}")
    private String exchangeName;
    //注入队列名称
    @Value("${mq.pay.queue.order}")
    private String queueName;
    //注入绑定key
    @Value("${mq.pay.routing.key}")
    private String key;

    /**
     * 创建队列
     *
     */
    @Bean
    public Queue orderQueue(){
        return new Queue(queueName);
    }
    /**
     * 创建交换机
     */
    @Bean
    public Exchange orderExchange(){
        //返回第一个参数：交换机名称  第二个参数：是否持久化  第三个参数：是否自动删除
        return new DirectExchange(exchangeName,true,false);
    }
    /**
     * 绑定
     */
    @Bean
    public Binding orderExchangeQueue(Queue orderQueue,Exchange orderExchange){
        //将queue消息队列绑定到exchange交换机上，绑定名称key当前队列名称
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(key).noargs();
    }
    /**************************************秒杀队列***************************************/



    //注入交换机名称
    @Value("${mq.pay.exchange.seckillorder}")
    private String seckillEexchangeName;
    //注入队列名称
    @Value("${mq.pay.queue.seckillorder}")
    private String seckillQueueName;
    //注入绑定key
    @Value("${mq.pay.routing.seckillkey}")
    private String seckillKey;

    /**
     * 创建队列
     *
     */
    @Bean
    public Queue orderSeckillQueue(){
        return new Queue(seckillQueueName);
    }
    /**
     * 创建交换机
     */
    @Bean
    public Exchange orderSeckillExchange(){
        //返回第一个参数：交换机名称  第二个参数：是否持久化  第三个参数：是否自动删除
        return new DirectExchange(seckillEexchangeName,true,false);
    }
    /**
     * 绑定
     */
    @Bean
    public Binding orderSeckillExchangeQueue(Queue orderSeckillQueue,Exchange orderSeckillExchange){
        //将queue消息队列绑定到exchange交换机上，绑定名称key当前队列名称
        return BindingBuilder.bind(orderSeckillQueue).to(orderSeckillExchange).with(seckillKey).noargs();
    }
}

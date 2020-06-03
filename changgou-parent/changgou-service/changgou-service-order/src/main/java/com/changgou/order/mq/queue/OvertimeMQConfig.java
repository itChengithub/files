package com.changgou.order.mq.queue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OvertimeMQConfig {
    /**
     * 定时队列，超时后将信息转发给listenerExchange交换机，将路由指向listenerQueue
     * @return
     */
    @Bean
    public Queue delayMessageQueue(){
        return QueueBuilder.durable("delayMessageQueue")//队列名称
                .withArgument("x-dead-letter-exchange","listenerExchange")//死信交换机dExchange将信息转给listenerExchange交换机
                .withArgument("x-dead-letter-routing-key","listenerQueue").build();//将路由的key指向监听队列listenerQueue
    }

    /**
     * 监听队列，，死信队列超时触发
     * @return
     */
    @Bean
    public Queue listenerQueue(){
        return new Queue("listenerQueue",true);
    }

    /**
     * 监听交换机
     */
    @Bean
    public Exchange listenerExchange(){
        return new DirectExchange("listenerExchange");
    }
//    @Bean
//    public Exchange xdeadletterexchange(){
//        return new DirectExchange("x-dead-letter-exchange");
//    }
    /**
     * 绑定监听交换机和监听队列
     */
    @Bean
    public Binding listenerBinding(Queue listenerQueue,Exchange listenerExchange){
        return BindingBuilder.bind(listenerQueue).to(listenerExchange).with("listenerQueue").noargs();
    }
//    @Bean
//    public Binding deadBinding(Queue dQueue,Exchange xdeadletterexchange){
//        return BindingBuilder.bind(dQueue).to(xdeadletterexchange).with("x-dead-letter-routing-key").noargs();
//    }
}

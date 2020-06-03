package com.changgou.seckill.mq.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.entity.SeckillStatus;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "${mq.pay.outTime.SeckillQueue}")
public class TimeOutListener {
    @Autowired
    private SeckillOrderMapper sckillOrderMapper;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @RabbitHandler
    public void timeOutListener(String message){
        if(message!=null){
            SeckillStatus seckillStatus = JSON.parseObject(message, SeckillStatus.class);
            SeckillOrder seckillOrder = sckillOrderMapper.selectByPrimaryKey(seckillStatus.getOrderId());
            if(seckillOrder!=null){
                if("1".equals(seckillOrder.getStatus())){
                    return;
                }else{
                    String username = seckillStatus.getUsername();
                    seckillOrderService.deleteOrder(username);
                }
            }
        }

    }
}

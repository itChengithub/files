package com.changgou.seckill.sysnc;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.entity.SeckillStatus;
import com.changgou.goods.util.IdWorker;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MultiThreadOrder {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${mq.pay.outTime.outTimeQueue}")
    private String outTimeQueue;
    @Async
    public void createOrder(){
        try {
            Thread.sleep(10000);
            System.out.println("县城睡着了");
            SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQuery").rightPop();
            if(seckillStatus!=null){
                String time=seckillStatus.getTime();
                Long goodsId=seckillStatus.getGoodsId();
                String username=seckillStatus.getUsername();
                SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("Seckill_" + time).get(goodsId);

                if(seckillGoods.getStockCount()<=0){
                    //400表示库存不足
                    throw new RuntimeException("400");

                }else {
                    //更新订单状态
                    seckillStatus.setStatus(4);
                    redisTemplate.boundHashOps("QueryOrderStatus").put(username,seckillStatus);
                }
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setId(new IdWorker().nextId());
                seckillOrder.setSeckillId(goodsId);
                seckillOrder.setMoney(seckillGoods.getPrice());
                seckillOrder.setUserId(username);
                seckillGoods.setCreateTime(new Date());
                seckillGoods.setStatus("0");
                redisTemplate.boundHashOps("seckillOrder").put(username,seckillOrder);
                //获取redis中此商品是否还有库存，没有的话同步数据库，删除redis
                Long size = redisTemplate.boundListOps("SeckillGoodsCountList_" + goodsId).size();
                if(size<=0){
                    seckillGoods.setStockCount(size.intValue());
                    seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                    //从Redis中删除
                    redisTemplate.boundHashOps("Seckill_" + time).delete(seckillGoods.getId());
                }else {
                    //商品同步到Redis
                    redisTemplate.boundHashOps("Seckill_" + time).put(seckillGoods.getId(),seckillGoods);
                }
                //更新订单状态
                seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
                seckillStatus.setStatus(2);
                seckillStatus.setOrderId(seckillOrder.getId());
                Map<String,String> map=new HashMap<String,String>();
                map.put("username",username);
                map.put("orderId",seckillOrder.getId().toString());
                redisTemplate.boundHashOps("QueryOrderStatus").put(username,seckillStatus);
                //创建延时队列，30分钟后删除订单
                rabbitTemplate.convertAndSend(outTimeQueue, (Object) JSON.toJSONString(seckillStatus), new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setExpiration("1800000");
                        return message;
                    }
                });
            }else {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

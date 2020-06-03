package com.changgou.order.service.impl;

import com.changgou.goods.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SpuFeign spuFeign;

    @Override
    public void deleteOrder(String username,Long skuId) {
        redisTemplate.boundHashOps("order_"+username).delete(skuId);
    }

    @Override
    @GlobalTransactional
    public void cartAdd(Long skuId, Integer count, String username) {
        //查询sku商品信息
        Result<Sku> skuResult = skuFeign.findById(skuId);
        Sku sku = skuResult.getData();
        //查询spu商品信息
        Result<Spu> spuResult = spuFeign.findById(Long.parseLong(sku.getSpuId()));
        Spu spu = spuResult.getData();
        OrderItem orderItem = createOrderItem(spu, sku, count);
        //将购物车数据内容存储到redis中
        redisTemplate.boundHashOps("order_"+username).put(skuId,orderItem);
    }

    @Override
    public List<OrderItem> findOrderByUserName(String username) {
        //获取知道那个命名空间下的所有数据
        return redisTemplate.boundHashOps("order_"+username).values();
    }

    /**
     * 封装order对象
     * @param spu
     * @param sku
     * @param count
     * @return
     */
    public OrderItem createOrderItem(Spu spu,Sku sku,Integer count){
        //封装购物车对象OrderItem
        OrderItem orderItem=new OrderItem();
        orderItem.setId(UUID.randomUUID().toString());
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSkuId(Long.parseLong(sku.getId()));
        orderItem.setSpuId(Long.parseLong(spu.getId()));
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setMoney(sku.getPrice()*count);
        orderItem.setNum(count);
        orderItem.setImage(sku.getImage());
        return orderItem;
    }
}

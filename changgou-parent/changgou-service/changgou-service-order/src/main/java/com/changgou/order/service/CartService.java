package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

public interface CartService {
    /**
     * 给用户添加收藏
     * @param skuId
     * @param count
     * @param username
     */
    void cartAdd(Long skuId,Integer count,String username);

    List<OrderItem> findOrderByUserName(String username);

    void deleteOrder(String username,Long skuId);
}

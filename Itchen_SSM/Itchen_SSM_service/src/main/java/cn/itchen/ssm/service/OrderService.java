package cn.itchen.ssm.service;

import cn.itchen.ssm.domain.Order;

import java.util.List;

/**
 * 订单的业务层接口
 */
public interface OrderService {
    /**
     * 获取所有订单
     * @return
     */
    List<Order> findAll();

    /**
     * 分页查询所有
     * @return
     */
    List<Order> findAllPage(int page,int size);

    Order findById(String id);
}

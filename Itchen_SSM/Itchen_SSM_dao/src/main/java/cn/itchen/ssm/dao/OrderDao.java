package cn.itchen.ssm.dao;

import cn.itchen.ssm.domain.Order;

import java.util.List;

/**
 * 订单的dao持久层接口
 */
public interface OrderDao {
    /**
     * 查询所有订单
     * @return
     */
    List<Order> findAll();

    /**
     * 根据id查询订单的详细信息
     * @param id
     * @return
     */
    Order findById(String id);
}

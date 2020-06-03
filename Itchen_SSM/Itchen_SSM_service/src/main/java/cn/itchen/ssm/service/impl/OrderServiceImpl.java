package cn.itchen.ssm.service.impl;

import cn.itchen.ssm.dao.OrderDao;
import cn.itchen.ssm.domain.Order;
import cn.itchen.ssm.service.OrderService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单的业务层实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    /**
     * 查询所有订单
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }

    @Override
    public List<Order> findAllPage(int page, int size) {
        PageHelper.startPage(page, size);
        return orderDao.findAll();
    }

    @Override
    public Order findById(String id) {
        return orderDao.findById(id);
    }
}

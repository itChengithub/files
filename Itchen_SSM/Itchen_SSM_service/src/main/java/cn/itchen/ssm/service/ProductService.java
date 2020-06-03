package cn.itchen.ssm.service;

import cn.itchen.ssm.domain.Product;

import java.util.List;

/**
 * 产品的service业务层接口
 */
public interface ProductService {
    /**
     * 查询所有产品信息
     * @return
     */
    List<Product> findAll();

    /**
     * 保存新添产品
     * @param product
     */
    void save(Product product);

    List<Product> findAllPage(int page, int size);
}

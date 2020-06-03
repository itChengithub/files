package cn.itchen.ssm.dao;

import cn.itchen.ssm.domain.Product;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品的dao持久层
 */
public interface ProductDao {
    /**
     * 获取所有产品
     * @return
     */
    List<Product> findAll();

    /**
     * 保存产品
     * @param product
     */
    void save(Product product);

    /**
     * 根据产品id获取产品信息
     * @param id
     * @return
     */
    Product findById(String id);
}

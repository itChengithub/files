package cn.itchen.ssm.service.impl;

import cn.itchen.ssm.dao.ProductDao;
import cn.itchen.ssm.domain.Product;
import cn.itchen.ssm.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品的service业务层实现类
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productdao;
    @Override
    public List<Product> findAll() {
        return productdao.findAll();
    }


    @Override
    public void save(Product product) {
        productdao.save(product);
    }

    @Override
    public List<Product> findAllPage(int page, int size) {
        PageHelper.startPage(page,size);
        return productdao.findAll();
    }
}

package com.changgou.goods.dao;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Template;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:传智播客
 * @Description:Category的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface CategoryMapper extends Mapper<Category> {
    /**
     * 根据分类id查询品牌集合
     * @param id
     * @return
     */
    @Select("SELECT b.* FROM  tb_brand b,tb_category_brand cb WHERE b.id=cb. brand_id AND cb.category_id=#{id}")
    List<Brand> findBrandById(Integer id);

    /**
     * 根据id查询模板信息
     * @param id
     * @return
     */
    @Select("select * from tb_template where category_id=#{id}")
    Template findTemplateById(Integer id);
}

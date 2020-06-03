package com.changgou.search.service.impl;

import com.changgou.search.dao.ContentCategoryMapper;
import com.changgou.search.pojo.ContentCategory;
import com.changgou.search.service.ContentCategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/****
 * @Author:传智播客
 * @Description:ContentCategory业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private ContentCategoryMapper categoryMapper;


    /**
     * ContentCategory条件+分页查询
     * @param contentCategory 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<ContentCategory> findPage(ContentCategory contentCategory, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(contentCategory);
        //执行搜索
        return new PageInfo<ContentCategory>(categoryMapper.selectByExample(example));
    }

    /**
     * ContentCategory分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<ContentCategory> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<ContentCategory>(categoryMapper.selectAll());
    }

    /**
     * ContentCategory条件查询
     * @param contentCategory
     * @return
     */
    @Override
    public List<ContentCategory> findList(ContentCategory contentCategory){
        //构建查询条件
        Example example = createExample(contentCategory);
        //根据构建的条件查询数据
        return categoryMapper.selectByExample(example);
    }


    /**
     * ContentCategory构建查询对象
     * @param contentCategory
     * @return
     */
    public Example createExample(ContentCategory contentCategory){
        Example example=new Example(ContentCategory.class);
        Example.Criteria criteria = example.createCriteria();
        if(contentCategory!=null){
            // 类目ID
            if(!StringUtils.isEmpty(contentCategory.getId())){
                    criteria.andEqualTo("id",contentCategory.getId());
            }
            // 分类名称
            if(!StringUtils.isEmpty(contentCategory.getName())){
                    criteria.andLike("name","%"+contentCategory.getName()+"%");
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改ContentCategory
     * @param contentCategory
     */
    @Override
    public void update(ContentCategory contentCategory){
        categoryMapper.updateByPrimaryKey(contentCategory);
    }

    /**
     * 增加ContentCategory
     * @param contentCategory
     */
    @Override
    public void add(ContentCategory contentCategory){
        categoryMapper.insert(contentCategory);
    }

    /**
     * 根据ID查询ContentCategory
     * @param id
     * @return
     */
    @Override
    public ContentCategory findById(Long id){
        return  categoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询ContentCategory全部数据
     * @return
     */
    @Override
    public List<ContentCategory> findAll() {
        return categoryMapper.selectAll();
    }
}

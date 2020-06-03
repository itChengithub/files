package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:传智播客
 * @Description:Spu业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SpuService {
    /**
     * 审核商品信息
     * @param spuId
     */
    void audit(Long spuId);
    /**
     * 修改商品信息
     * @param goods
     */
    void updateGoods(Goods goods);
    /**
     * 根据id查询goods信息
     * @param id
     * @return
     */
    Goods findGoodsById(Long id);
    /**
     * 保存goods，一个spu和一个集合sku
     * @param goods
     */
    void saveSkuAndParentSpu(Goods goods);
    /***
     * Spu多条件分页查询
     * @param spu
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(Spu spu, int page, int size);

    /***
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(int page, int size);

    /***
     * Spu多条件搜索方法
     * @param spu
     * @return
     */
    List<Spu> findList(Spu spu);

    /***
     * 删除Spu
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Spu数据
     * @param spu
     */
    void update(Spu spu);

    /***
     * 新增Spu
     * @param spu
     */
    void add(Spu spu);

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
     Spu findById(Long id);

    /***
     * 查询所有Spu
     * @return
     */
    List<Spu> findAll();

    /**
     * 将商品下架
     * @param spuId
     */
    void pull(Long spuId);

    void put(Long spuId);

    void putAll(Long[] spuIds);
}
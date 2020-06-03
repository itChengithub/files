package com.changgou.seckill.service.impl;

import com.changgou.goods.entity.Result;
import com.changgou.goods.entity.SeckillStatus;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.util.DateUtil;
import com.changgou.goods.util.IdWorker;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillGoodsService;
import com.changgou.seckill.service.SeckillOrderService;
import com.changgou.seckill.sysnc.MultiThreadOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/****
 * @Author:传智播客
 * @Description:SeckillOrder业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    /**
     * 删除用户订单，回滚库存（）
     * @param username
     */
    @Override
    @GlobalTransactional
    public void deleteOrder(String username) {
        //删除订单信息
        redisTemplate.boundHashOps("seckillOrder").delete(username);
        //修改用户排队状态
        SeckillStatus orderStatus = (SeckillStatus) redisTemplate.boundHashOps("QueryOrderStatus").get(username);
        orderStatus.setStatus(4);
        //删除用户排队信息
        clearUserQueue(username);
        //回滚库存
        //商品id
        Long goodsId = orderStatus.getGoodsId();
        //商品所属秒杀时区
        String time = orderStatus.getTime();
        //更新redis
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("Seckill_" + time).get(goodsId);
        //可能商品已卖完，更新数据库库存
        if(seckillGoods==null){
            seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
            seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
        }else{
            seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
        }
        redisTemplate.boundHashOps("Seckill_" + time).put(goodsId,seckillGoods);
        //添加队列
        redisTemplate.boundListOps("SeckillGoodsCountList_"+seckillGoods.getId()).leftPush(seckillGoods.getId());

    }

    /**
     * SeckillOrder条件+分页查询
     * @param seckillOrder 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(seckillOrder);
        //执行搜索
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    /**
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    /**
     * SeckillOrder条件查询
     * @param seckillOrder
     * @return
     */
    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder){
        //构建查询条件
        Example example = createExample(seckillOrder);
        //根据构建的条件查询数据
        return seckillOrderMapper.selectByExample(example);
    }


    /**
     * SeckillOrder构建查询对象
     * @param seckillOrder
     * @return
     */
    public Example createExample(SeckillOrder seckillOrder){
        Example example=new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if(seckillOrder!=null){
            // 主键
            if(!StringUtils.isEmpty(seckillOrder.getId())){
                    criteria.andEqualTo("id",seckillOrder.getId());
            }
            // 秒杀商品ID
            if(!StringUtils.isEmpty(seckillOrder.getSeckillId())){
                    criteria.andEqualTo("seckillId",seckillOrder.getSeckillId());
            }
            // 支付金额
            if(!StringUtils.isEmpty(seckillOrder.getMoney())){
                    criteria.andEqualTo("money",seckillOrder.getMoney());
            }
            // 用户
            if(!StringUtils.isEmpty(seckillOrder.getUserId())){
                    criteria.andEqualTo("userId",seckillOrder.getUserId());
            }
            // 创建时间
            if(!StringUtils.isEmpty(seckillOrder.getCreateTime())){
                    criteria.andEqualTo("createTime",seckillOrder.getCreateTime());
            }
            // 支付时间
            if(!StringUtils.isEmpty(seckillOrder.getPayTime())){
                    criteria.andEqualTo("payTime",seckillOrder.getPayTime());
            }
            // 状态，0未支付，1已支付
            if(!StringUtils.isEmpty(seckillOrder.getStatus())){
                    criteria.andEqualTo("status",seckillOrder.getStatus());
            }
            // 收货人地址
            if(!StringUtils.isEmpty(seckillOrder.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",seckillOrder.getReceiverAddress());
            }
            // 收货人电话
            if(!StringUtils.isEmpty(seckillOrder.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",seckillOrder.getReceiverMobile());
            }
            // 收货人
            if(!StringUtils.isEmpty(seckillOrder.getReceiver())){
                    criteria.andEqualTo("receiver",seckillOrder.getReceiver());
            }
            // 交易流水
            if(!StringUtils.isEmpty(seckillOrder.getTransactionId())){
                    criteria.andEqualTo("transactionId",seckillOrder.getTransactionId());
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
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改SeckillOrder
     * @param seckillOrder
     */
    @Override
    public void update(SeckillOrder seckillOrder){
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }



    /**
     * 根据ID查询SeckillOrder
     * @param id
     * @return
     */
    @Override
    public SeckillOrder findById(Long id){
        return  seckillOrderMapper.selectByPrimaryKey(id);
    }
    /**
     * 查询SeckillOrder全部数据
     * @return
     */
    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderMapper.selectAll();
    }
    @Autowired
    private MultiThreadOrder multiThreadOrder;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    @GlobalTransactional
    public Boolean add(String username, String time, Long goodsId) {
        //记录用户排队次数，如多次排队抛出异常     返回值是用户排队的次数
        Long count = redisTemplate.boundHashOps("UserQueryCount").increment(username, 1);
        if(count>1){
            //排队次数大于1 表示已经排过队了，抛出异常
            throw new RuntimeException("用户重复排队");
        }
        Long stockCount = (Long) redisTemplate.boundListOps("SeckillGoodsCountList_"+goodsId).rightPop();
        if(stockCount==null){
            redisTemplate.boundHashOps("Seckill_"+time).delete(goodsId);
            redisTemplate.boundHashOps("UserQueryCount").delete(username);
            return false;
        }
        SeckillStatus seckillStatus = new SeckillStatus(username,time,goodsId,new Date(),1);
        //往redis存一个下单状态
        redisTemplate.boundHashOps("QueryOrderStatus").put(username,seckillStatus);
        redisTemplate.boundListOps("SeckillOrderQuery").leftPush(seckillStatus);
        multiThreadOrder.createOrder();

        return true;
    }

    @Override
    public SeckillStatus queryStatus(String username) {
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps("QueryOrderStatus").get(username);
        return seckillStatus;
    }
    @Override
    @GlobalTransactional
    public void payOk(String username, String transactionId, String payTime) {
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(username);
        seckillOrder.setStatus("1");
        seckillOrder.setPayTime(DateUtil.payDateFormat(payTime));
        seckillOrder.setTransactionId(transactionId);
        seckillOrderMapper.insertSelective(seckillOrder);
        //删除redis中的订单信息
        redisTemplate.boundHashOps("seckillOrder").delete(username);
        //清除用户排队信息
        clearUserQueue(username);
    }

    /**
     * 删除用户排队信息
     * @param username
     */
    public void clearUserQueue(String username){
        redisTemplate.boundHashOps("UserQueryCount").delete(username);
    }
}

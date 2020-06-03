package com.changgou.goods.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 封装用户抢单信息
 */
public class SeckillStatus implements Serializable {
    private String username;//用户名
    private String time;//抢单时区
    private Long goodsId;//商品id
    private Date createTime;//创建订单时间
    private Integer status;//排队状态  1： 排队中   2：秒杀等待支付   3：支付超市  4：秒杀失败  5：支付完成
    private Float money;//应付金额
    private Long orderId;//订单号

    public SeckillStatus(String username, String time, Long goodsId, Date createTime, Integer status) {
        this.username = username;
        this.time = time;
        this.goodsId = goodsId;
        this.createTime = createTime;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SeckillStatus() {
    }

    public SeckillStatus(String username, String time, Long goodsId) {
        this.username = username;
        this.time = time;
        this.goodsId = goodsId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}

package com.changgou.seckill.controller;

import com.changgou.goods.entity.SeckillStatus;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
import com.github.pagehelper.PageInfo;
import com.changgou.goods.entity.Result;
import com.changgou.goods.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:传智播客
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/seckill/order")
@CrossOrigin
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;
    /**
     * 枪弹状态查询
     */
    @GetMapping("/query")
    public Result<SeckillStatus> queryStatus(String username){
        SeckillStatus seckillStatus=seckillOrderService.queryStatus(username);
        if(seckillStatus==null){
            return new Result<SeckillStatus>(false,StatusCode.NOTFOUNDERROR,"您没有参与任何抢单");
        }
        if(seckillStatus.getStatus()==2){
            return new Result<SeckillStatus>(true,StatusCode.OK,"秒杀等待支付",seckillStatus);
        }else if(seckillStatus.getStatus()==1){
            return new Result<SeckillStatus>(true,StatusCode.OK,"排队中",seckillStatus);
        }else if(seckillStatus.getStatus()==4){
            return new Result<SeckillStatus>(true,StatusCode.OK,"秒杀失败",seckillStatus);
        }
        return new Result<SeckillStatus>(false,StatusCode.NOTFOUNDERROR,"未查询到你的订单状态");
    }
    /**
     * 处理秒杀订单
     */
    @GetMapping("add")
    public Result<Boolean> add(String username,String time,Long goodsId){
        Boolean flag=seckillOrderService.add(username,time,goodsId);
        if(flag){
            return new Result<Boolean>(true,StatusCode.OK,"排队处理中......请稍后");
        }else {
            return new Result<Boolean>(false,StatusCode.ERROR,"下单失败！商品无库存");
        }
    }
    /***
     * SeckillOrder分页条件搜索实现
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  SeckillOrder seckillOrder, @PathVariable  int page, @PathVariable  int size){
        //调用SeckillOrderService实现分页条件查询SeckillOrder
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(seckillOrder, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * SeckillOrder分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SeckillOrderService实现分页查询SeckillOrder
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param seckillOrder
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<SeckillOrder>> findList(@RequestBody(required = false)  SeckillOrder seckillOrder){
        //调用SeckillOrderService实现条件查询SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findList(seckillOrder);
        return new Result<List<SeckillOrder>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SeckillOrderService实现根据主键删除
        seckillOrderService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  SeckillOrder seckillOrder,@PathVariable Long id){
        //设置主键值
        seckillOrder.setId(id);
        //调用SeckillOrderService实现修改SeckillOrder
        seckillOrderService.update(seckillOrder);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 根据ID查询SeckillOrder数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SeckillOrder> findById(@PathVariable Long id){
        //调用SeckillOrderService实现根据主键查询SeckillOrder
        SeckillOrder seckillOrder = seckillOrderService.findById(id);
        return new Result<SeckillOrder>(true,StatusCode.OK,"查询成功",seckillOrder);
    }

    /***
     * 查询SeckillOrder全部数据
     * @return
     */
    @GetMapping
    public Result<List<SeckillOrder>> findAll(){
        //调用SeckillOrderService实现查询所有SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findAll();
        return new Result<List<SeckillOrder>>(true, StatusCode.OK,"查询成功",list) ;
    }
}

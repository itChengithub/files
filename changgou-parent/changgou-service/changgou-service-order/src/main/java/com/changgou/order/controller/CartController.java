package com.changgou.order.controller;

import com.changgou.goods.entity.Result;
import com.changgou.goods.entity.StatusCode;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @GetMapping("add")
    public Result cartAdd(Long skuId,Integer count,String username){
        if (count<=0){
            cartService.deleteOrder("szitheima",skuId);
            return new Result(true,StatusCode.OK,"删除收藏成功");
        }
        cartService.cartAdd(skuId,count,"szitheima");
        return new Result(true, StatusCode.OK,"添加购物车成功");
    }
    @GetMapping("get/{username}")
    public Result<List<OrderItem>> findOrderByUserName(@PathVariable("username") String username){
        List<OrderItem> orderItemList=cartService.findOrderByUserName(username);
        return new Result<List<OrderItem>>(true,StatusCode.OK,"查询"+username+"购物车成功",orderItemList);
    }
}

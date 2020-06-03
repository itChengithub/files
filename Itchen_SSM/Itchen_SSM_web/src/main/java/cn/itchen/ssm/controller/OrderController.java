package cn.itchen.ssm.controller;

import cn.itchen.ssm.domain.Order;
import cn.itchen.ssm.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService service;
    @Autowired
    private ModelAndView modelAndView;
    @RequestMapping("/findAll")
    public ModelAndView findAll(){
        List<Order> orders = service.findAll();
        modelAndView.addObject("ordersList",orders);
        modelAndView.setViewName("order-list");
        return modelAndView;
    }
    @RequestMapping("/findAllPage")
    public ModelAndView findAllPage(@RequestParam(name = "page",required = true,defaultValue = "1")Integer page,@RequestParam(name = "size",required = true,defaultValue = "5")Integer size){
        List<Order> orders = service.findAllPage(page, size);
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        pageInfo.setSize(size);
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.setViewName("order-list");
        return modelAndView;
    }
    @RequestMapping("/findById")
    public ModelAndView findById(@RequestParam(name = "id",required = true)String id){
        Order order=service.findById(id);
        modelAndView.addObject("order",order);
        modelAndView.setViewName("order-details");
        return modelAndView;
    }
}

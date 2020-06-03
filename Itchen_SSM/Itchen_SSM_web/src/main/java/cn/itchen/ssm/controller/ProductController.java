package cn.itchen.ssm.controller;

import cn.itchen.ssm.domain.Product;
import cn.itchen.ssm.service.ProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private ModelAndView modelAndView;
    @RequestMapping("/findAll")
    public ModelAndView findAll(){
        List<Product> products = service.findAll();
        modelAndView.addObject("productList",products);
        modelAndView.setViewName("product-list");
        return modelAndView;
    }
    @RequestMapping("/save")
    @RolesAllowed({"ROLE_USER","ROLE_ADMIN","ROLE_MEMBER"})
    public String save(Product product){
        service.save(product);
        return "forward:findAll";
    }
    @RequestMapping("/findAllPage")
    public ModelAndView findAllPage(@RequestParam(name = "page",required = true,defaultValue = "1")Integer page, @RequestParam(name = "size",required = true,defaultValue = "5")Integer size){
        List<Product> products = service.findAllPage(page,size);
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        pageInfo.setSize(size);
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.setViewName("product-list");
        return modelAndView;
    }
}

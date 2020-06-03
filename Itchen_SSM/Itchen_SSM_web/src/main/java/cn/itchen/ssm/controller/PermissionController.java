package cn.itchen.ssm.controller;

import cn.itchen.ssm.domain.Permission;
import cn.itchen.ssm.service.PermissionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService service;
    @Autowired
    private ModelAndView modelAndView;
    @RequestMapping("/findAll")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ModelAndView findAll(@RequestParam(value = "page",required = true,defaultValue = "1") Integer page,@RequestParam(value = "size",required = true,defaultValue = "5") Integer size){
        List<Permission> permissions = service.findAll(page, size);
        PageInfo<Permission> pageInfo = new PageInfo<>(permissions);
        pageInfo.setSize(size);
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.setViewName("permission-list");
        return modelAndView;
    }
    @RequestMapping("/save")
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    public String save(Permission permission){
        service.save(permission);
        return "forward:findAll";
    }
    @Secured({"ROLE_ADMIN"})
    @RequestMapping("/delete")
    public String delete(String id){
        service.delete(id);
        return "forward:findAll";
    }
    @RequestMapping("findById")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ModelAndView findById(String id){
        Permission permission=service.findById(id);
        modelAndView.addObject("permission",permission);
        modelAndView.setViewName("permission-show");
        return modelAndView;
    }
}

package cn.itchen.ssm.controller;

import cn.itchen.ssm.domain.Permission;
import cn.itchen.ssm.domain.Role;
import cn.itchen.ssm.service.RoleService;
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
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService service;
    @Autowired
    private ModelAndView modelAndView;
    @RequestMapping("/findAll")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ModelAndView findAll(@RequestParam(value = "page",required = true,defaultValue = "1") Integer page,@RequestParam(value = "size",required = true,defaultValue = "5") Integer size){
        List<Role> roles = service.findAll(page, size);
        PageInfo<Role> pageInfo = new PageInfo<>(roles);
        pageInfo.setSize(size);
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.setViewName("role-list");
        return modelAndView;
    }
    @RequestMapping("/save")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public String save(Role role){
        service.save(role);
        return "forward:findAll";
    }
    @RequestMapping("/delete")
    @Secured("ROLE_ADMIN")
    public String delete(String id){
        service.delete(id);
        return "forward:findAll";
    }
    @RequestMapping("/findById")
    @RolesAllowed({"ROLE_ADMIN","ROLE_USER"})
    public ModelAndView findById(String id){
        Role role=service.findById(id);
        modelAndView.addObject("role",role);
        modelAndView.setViewName("role-show");
        return modelAndView;
    }
    @RequestMapping("/findUserByIdAndAllRole")
    @Secured("ROLE_ADMIN")
    public ModelAndView findUserByIdAndAllRole(String id){
        Role role=service.findById(id);
        List<Permission> permissions=service.findOtherPermissions(id);
        modelAndView.addObject("role",role);
        modelAndView.addObject("permissions",permissions);
        modelAndView.setViewName("role-permission-add");
        return modelAndView;
    }
    @RequestMapping("/addPermissionToRole")
    @Secured("ROLE_ADMIN")
    public String addPermissionToRole(@RequestParam(value = "roleId")String roleId,@RequestParam(value = "ids")String[] permissionIds){
        service.addPermissionToRole(roleId,permissionIds);
        return "forward:findAll";
    }
}

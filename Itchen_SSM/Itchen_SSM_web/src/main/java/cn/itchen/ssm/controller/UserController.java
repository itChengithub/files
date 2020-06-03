package cn.itchen.ssm.controller;

import cn.itchen.ssm.domain.Role;
import cn.itchen.ssm.domain.UserInfo;
import cn.itchen.ssm.service.UserService;
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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private ModelAndView modelAndView;
    @RequestMapping("/findAll")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ModelAndView findAll(@RequestParam(value = "page",required = true,defaultValue = "1")Integer page,@RequestParam(value = "size",required = true,defaultValue = "5")Integer size){
        List<UserInfo> users=service.findAll(page,size);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(users);
        pageInfo.setSize(size);
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.setViewName("user-list");
        return modelAndView;
    }
    @RequestMapping("/save")
    @RolesAllowed({"ROLE_ADMIN","ROLE_USER"})
    public String save(UserInfo userInfo){
        service.save(userInfo);
        return "forward:findAll";
    }
    @RequestMapping("/findById")
    @RolesAllowed({"ROLE_ADMIN","ROLE_USER"})
    public ModelAndView findById(String id){
        UserInfo user=service.findById(id);
        modelAndView.addObject("user",user);
        modelAndView.setViewName("user-show1");
        return modelAndView;
    }
    @RequestMapping("delete")
    @Secured("ROLE_ADMIN")
    public String delete(@RequestParam(value = "id",required = true) String id){
        service.delete(id);
        return "forward:findAll";
    }
    @RequestMapping("/findUserByIdAndAllRole")
    @Secured("ROLE_ADMIN")
    public ModelAndView findUserByIdAndAllRole(String id){
        UserInfo user = service.findById(id);
        List<Role> roles=service.findOtherRole(id);
        modelAndView.addObject("user",user);
        modelAndView.addObject("roles",roles);
        modelAndView.setViewName("user-role-add");
        return modelAndView;
    }
    @RequestMapping("/addRoleToUser")
    @Secured("ROLE_ADMIN")
    public String addRoleToUser(@RequestParam(value = "userId") String userId,@RequestParam(value = "ids") String[] roleIds){
        service.andRoleToUser(userId,roleIds);
        return "forward:findAll";
    }
    @RequestMapping("/changeStatus")
    @Secured("ROLE_ADMIN")
    public String changeStatus(String id){
        service.changeStatus(id);
        return "forward:findAll";
    }
}

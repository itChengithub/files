package cn.itchen.controller;

import cn.itchen.entity.User;
import cn.itchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    @PostMapping("/login")
    public String login(String username, String password, HttpServletRequest request){
        User loginUser = service.login(username, password);
        if(loginUser!=null){
            request.getSession().setAttribute("loginUser",loginUser);
            return "redirect:/file/showAll";
        }
        request.getSession().setAttribute("error","登陆失败！请检查您的用户名密码");
        return "login";
    }
}

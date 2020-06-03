package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.entity.Result;
import com.changgou.goods.entity.StatusCode;
import com.changgou.goods.util.JwtUtil;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/****
 * @Author:传智播客
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * 登录方法
     */
    @GetMapping("/login")
    public Result<User> login(@RequestParam String username, String password, HttpServletResponse response){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return new Result<>(false,StatusCode.LOGINERROR,"用户名或密码不能为空！");
        }
        User user = userService.findById(username);
        if(user!=null){
            if(BCrypt.checkpw(password,user.getPassword())){
                Map<String,Object> map=new HashMap<>();
                map.put("role","user");
                map.put("success","SUCCESS");
                map.put("username",username);
                //jwt令牌工具类： 第一个参数：令牌的id  第二个参数 ： JSON格式的Map集合，封装一些数据 第三个参数：超时时间，默认1小时
                String token = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(map), null);
                //获取Cookie，添加Jwt令牌cookie
                Cookie cookie = new Cookie("Authorization",token);
                //设置创建cookie的IP
                cookie.setDomain("localhost");
                //cookie的存储路径
                cookie.setPath("/");
                response.addCookie(cookie);
                return new Result<User>(true,StatusCode.OK,"登录成功",token);
            }
        }
        return new Result<>(false,StatusCode.LOGINERROR,"登录失败");
    }
    /***
     * User分页条件搜索实现
     * @param user
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  User user, @PathVariable  int page, @PathVariable  int size){
        //调用UserService实现分页条件查询User
        PageInfo<User> pageInfo = userService.findPage(user, page, size);
        return new Result(true, StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * User分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用UserService实现分页查询User
        PageInfo<User> pageInfo = userService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param user
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<User>> findList(@RequestBody(required = false)  User user){
        //调用UserService实现条件查询User
        List<User> list = userService.findList(user);
        return new Result<List<User>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        //调用UserService实现根据主键删除
        userService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改User数据
     * @param user
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  User user,@PathVariable String id){
        //设置主键值
        user.setUsername(id);
        //调用UserService实现修改User
        userService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增User数据
     * @param user
     * @return
     */
    @PostMapping
    public Result add(@RequestBody   User user){
        //调用UserService实现添加User
        userService.add(user);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询User数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable String id){
        //调用UserService实现根据主键查询User
        User user = userService.findById(id);
        return new Result<User>(true,StatusCode.OK,"查询成功",user);
    }

    /***
     * 查询User全部数据
     * @return
     */
    @GetMapping
    public Result<List<User>> findAll(){
        //调用UserService实现查询所有User
        List<User> list = userService.findAll();
        return new Result<List<User>>(true, StatusCode.OK,"查询成功",list) ;
    }
    @RequestMapping("/points/add")
    public Result addPoints(String username){
        Boolean flag=userService.addPoints(username);
        String res=flag==true?"添加积分成功":"添加积分失败";
        if(flag){
            return new Result(true,StatusCode.OK,res);
        }
        return new Result(false,StatusCode.ERROR,res);
    }
}

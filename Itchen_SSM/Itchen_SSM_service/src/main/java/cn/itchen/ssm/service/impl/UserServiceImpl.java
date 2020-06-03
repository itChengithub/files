package cn.itchen.ssm.service.impl;

import cn.itchen.ssm.dao.UserDao;
import cn.itchen.ssm.domain.Role;
import cn.itchen.ssm.domain.UserInfo;
import cn.itchen.ssm.service.UserService;
import cn.itchen.ssm.utils.BCryptPasswordEncoderUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户认证的Service业务层实现类
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userDao.findByUsername(username);
        //我们的密码如果没有加密的话必须在前面加上"{noop}"的前缀表明他是没有加密的密码
        User user=new User(userInfo.getUsername(),userInfo.getPassword(),userInfo.getStatus()!=0,true,true,true,getGrantedAuthoritys(userInfo.getRoles()));
        return user;
    }
    public List<SimpleGrantedAuthority> getGrantedAuthoritys(List<Role> roles){
        List<SimpleGrantedAuthority> list=new ArrayList<>();
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return list;
    }

    @Override
    public List<UserInfo> findAll(int page,int size) {
        PageHelper.startPage(page,size);
        return userDao.findAll();
    }

    @Override
    public void save(UserInfo userInfo) {
        //给密码加密
        userInfo.setPassword(BCryptPasswordEncoderUtils.encodePassword(userInfo.getPassword()));
        userDao.save(userInfo);
    }

    @Override
    public UserInfo findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public void delete(String id) {
        //我们需要定义一个方法让其先删除中间表
        userDao.deleteUsers_role(id);
        userDao.delete(id);
    }

    @Override
    public List<Role> findOtherRole(String id) {

        return userDao.findOtherRole(id);
    }

    @Override
    public void andRoleToUser(String userId, String[] roleIds) {
        for(String roleId:roleIds){
            userDao.addRoleToUser(userId,roleId);
        }
    }

    @Override
    public void changeStatus(String id) {
        userDao.changeStatus(id);
    }
}

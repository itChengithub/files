package cn.itchen.ssm.service;

import cn.itchen.ssm.domain.Role;
import cn.itchen.ssm.domain.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserInfo> findAll(int page,int size);

    void save(UserInfo userInfo);

    UserInfo findById(String id);

    void delete(String id);

    List<Role> findOtherRole(String id);

    void andRoleToUser(String userId, String[] roleIds);

    void changeStatus(String id);
}

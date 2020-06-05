package cn.itchen.service;

import cn.itchen.entity.User;

public interface UserService {
    User login(String username,String password);
}

package cn.itchen.service.impl;

import cn.itchen.dao.UserDao;
import cn.itchen.entity.User;
import cn.itchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User login(String username, String password) {
        return userDao.login(username, password);
    }
}

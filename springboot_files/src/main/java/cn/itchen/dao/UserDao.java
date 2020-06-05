package cn.itchen.dao;

import cn.itchen.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户持久层
 */
public interface UserDao {
    User login(@Param("username") String username,@Param("password") String password);
}

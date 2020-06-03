package cn.itchen.ssm.dao;

import cn.itchen.ssm.domain.Role;
import cn.itchen.ssm.domain.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取用户信息的dao
 */
public interface UserDao {
    /**
     * 根据用户名查询用户所有信息
     * @param username
     * @return
     */
    UserInfo findByUsername(String username);

    /**
     * 查询所有用户
     * @return
     */
    List<UserInfo> findAll();

    /**
     * 添加用户
     * @param userInfo
     */
    void save(UserInfo userInfo);

    /**
     * 根据用户id查询用户所有信息
     * @param id
     * @return
     */
    UserInfo findById(String id);

    /**
     * 根据角色id查询拥有该角色的用户信息
     * @param rid
     * @return
     */
    List<UserInfo> findByRid(String rid);

    /**
     * 在删除用户之前我们应该删除中间表有关该用户的角色关联
     * @param id
     */
    void deleteUsers_role(String id);

    /**
     * 删除用户
     * @param id
     */
    void delete(String id);


    List<Role> findOtherRole(String id);

    /**
     * 根据用户和角色的id给用户添加角色
     * @param userId
     * @param roleId
     */
    void addRoleToUser(@Param("userId") String userId,@Param("roleId") String roleId);

    void changeStatus(String id);
}

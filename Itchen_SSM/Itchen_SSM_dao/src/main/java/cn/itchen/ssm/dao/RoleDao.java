package cn.itchen.ssm.dao;

import cn.itchen.ssm.domain.Permission;
import cn.itchen.ssm.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 查询所有角色
 */
public interface RoleDao {
    /**
     * 根据用户id查询用户所拥有的角色信息
     * @param uid
     * @return
     */
    List<Role> findByUid(String uid);

    /**
     * 根据权限id查询拥有权限的角色集合
     * @param pid
     * @return
     */
    List<Role> findByPid(String pid);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();

    /**
     * 保存新添加角色
     * @param role
     */
    void save(Role role);

    /**
     * 删除用户和此id的中间表
     * @param id
     */
    void deleteU_R(String id);

    /**
     * 删除权限和此id的中间表修
     * @param id
     */
    void deleteR_P(String id);

    /**
     * 删除角色表
     * @param id
     */
    void delete(String id);

    /**
     * 根据角色id查询角色
     * @param id
     * @return
     */
    Role findById(String id);

    /**
     * 根据角色id查询其未拥有的权限
     * @param id
     * @return
     */
    List<Permission> findOtherPermissions(String id);

    /**
     * 根据id给角色添加权限
     * @param roleId
     * @param permissionId
     */
    void addPermissionToRole(@Param("roleId") String roleId,@Param("permissionId") String permissionId);
}

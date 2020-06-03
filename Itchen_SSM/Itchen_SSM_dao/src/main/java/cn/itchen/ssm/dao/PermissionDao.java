package cn.itchen.ssm.dao;

import cn.itchen.ssm.domain.Permission;

import java.util.List;

/**
 * 权限持久层接口
 */
public interface PermissionDao {
    /**
     * 根据角色id查询其所有权限
     * @param rid
     * @return
     */
    List<Permission> findByRid(String rid);

    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAll();

    /**
     * 保存新添加的权限
     * @param permission
     */
    void save(Permission permission);

    /**
     * 删除中间表相关信息
     * @param id
     */
    void deleteR_P(String id);

    /**
     * 删除权限
     * @param id
     */
    void delete(String id);

    /**
     * 根据id查询权限
     * @param id
     * @return
     */
    Permission findById(String id);
}

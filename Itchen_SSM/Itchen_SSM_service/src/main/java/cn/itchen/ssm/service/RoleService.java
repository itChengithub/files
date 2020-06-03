package cn.itchen.ssm.service;

import cn.itchen.ssm.domain.Permission;
import cn.itchen.ssm.domain.Role;

import java.util.List;

/**
 * 角色service业务层接口
 */
public interface RoleService {
    List<Role> findAll(int page,int size);

    void save(Role role);

    void delete(String id);

    Role findById(String id);

    List<Permission> findOtherPermissions(String id);

    void addPermissionToRole(String roleId, String[] permissionIds);
}

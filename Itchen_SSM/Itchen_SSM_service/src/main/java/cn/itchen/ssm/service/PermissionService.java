package cn.itchen.ssm.service;

import cn.itchen.ssm.domain.Permission;

import java.util.List;

/**
 * 权限信息业务层接口
 */
public interface PermissionService {
    /**
     * 查询所有权限信息
     * @param page
     * @param size
     * @return
     */
    List<Permission> findAll(int page,int size);

    void save(Permission permission);

    void delete(String id);

    Permission findById(String id);
}

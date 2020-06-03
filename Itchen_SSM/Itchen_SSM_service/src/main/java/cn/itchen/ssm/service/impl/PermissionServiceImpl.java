package cn.itchen.ssm.service.impl;

import cn.itchen.ssm.dao.PermissionDao;
import cn.itchen.ssm.domain.Permission;
import cn.itchen.ssm.service.PermissionService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public List<Permission> findAll(int page, int size) {
        PageHelper.startPage(page, size);
        return permissionDao.findAll();
    }

    @Override
    public void save(Permission permission) {
        permissionDao.save(permission);
    }

    @Override
    public void delete(String id) {
        //先删除关联表
        permissionDao.deleteR_P(id);
        permissionDao.delete(id);
    }

    @Override
    public Permission findById(String id) {
        return permissionDao.findById(id);
    }
}

package cn.itchen.ssm.service.impl;

import cn.itchen.ssm.dao.RoleDao;
import cn.itchen.ssm.domain.Permission;
import cn.itchen.ssm.domain.Role;
import cn.itchen.ssm.service.RoleService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Override
    public List<Role> findAll(int page, int size) {
        PageHelper.startPage(page,size);
        return roleDao.findAll();
    }

    @Override
    public void save(Role role) {
        roleDao.save(role);
    }

    @Override
    public void delete(String id) {
        roleDao.deleteU_R(id);
        roleDao.deleteR_P(id);
        roleDao.delete(id);
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Permission> findOtherPermissions(String id) {
        return roleDao.findOtherPermissions(id);
    }

    @Override
    public void addPermissionToRole(String roleId, String[] permissionIds) {
        for(String permissionId:permissionIds){
            roleDao.addPermissionToRole(roleId,permissionId);
        }
    }
}

package cn.itchen.ssm.test.TestRoles;

import cn.itchen.ssm.dao.PermissionDao;
import cn.itchen.ssm.dao.RoleDao;
import cn.itchen.ssm.dao.UserDao;
import cn.itchen.ssm.domain.Permission;
import cn.itchen.ssm.domain.Role;
import cn.itchen.ssm.domain.UserInfo;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationConfig_dao.xml")
public class TestFIndByUid {
    @Autowired
    private PermissionDao permissionDao;
    @Test
    public void testUid(){
        Permission p001 = permissionDao.findById("p001");
        List<Role> roles = p001.getRoles();
        for (Role role:roles){
            System.out.println(role.getRoleName());
        }
    }
}

package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired//Spring框架提供的依赖注入注解   先byType再byName
 // @Resource //JDK提供的依赖注入注解   先byName再byType
            RoleDao roleDao;

    @Autowired
    AdminRoleDao adminRoleDao;

    @Override
    public BaseDao<Role> getEntityDao() {
        return roleDao;
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    //根据用户获取角色数据
    @Override
    public Map<String, Object> findRoleByAdminId(Long adminId) {
        //查询所有的角色
        List<Role> allRole = roleDao.findAll();

        //拥有的角色id
        List<Long> existRoleIdList = adminRoleDao.findRoleIdByAdminId(adminId);

        //对角色进行分类
        List<Role> assignRoleList = new ArrayList<>();
        List<Role> noAssignRoleList = new ArrayList<>();
        for (Role role : allRole) {
            if (existRoleIdList.contains(role.getId())) {
                assignRoleList.add(role);
            } else {
                noAssignRoleList.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList",assignRoleList);
        roleMap.put("noAssignRoleList",noAssignRoleList);

        return roleMap;
    }

    //分配角色
    @Override
    public void saveAdminRoleRelationShip(Long adminId, Long[] roleIds) {
        adminRoleDao.deleteByAdminId(adminId);

        List<AdminRole> adminRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            if (StringUtils.isEmpty(roleId)) continue;
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleList.add(adminRole);
        }

        adminRoleDao.insertBatch(adminRoleList);

    }


}

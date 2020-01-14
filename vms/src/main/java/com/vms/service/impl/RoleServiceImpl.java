package com.vms.service.impl;

import com.vms.bean.Permission;
import com.vms.bean.Role;
import com.vms.dao.PermissionMapper;
import com.vms.dao.RoleMapper;
import com.vms.service.MenuService;
import com.vms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    MenuService menuService;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Override
    public List<Role> findAll(Role role) {
        return roleMapper.findAll(role);
    }

    @Override
    public List<Role> findAllAndUser(Role role) {
        return roleMapper.findAllAndUser(role);
    }

    @Override
    public List<Role> findByUserId(int user_id, String name) {
        return roleMapper.findByUserId(user_id,name);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
    public void addRoleAndMenu(String name,String remark,ArrayList menu) throws Exception {
        Role role = new Role(){{
            setName(name);
            setRemark(remark);
        }};
        System.out.println(role);
        roleMapper.insertRole(role);
        for (int i = 0; i < menu.size(); i++) {
            roleMapper.insertRoleMenu(role.getId(), (int)menu.get(i));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
    public void updateRoleAndPermit(Role role) throws Exception {
        List<Permission> permissionList = role.getPermissionList();
        List<Permission> dbPermissionList = permissionMapper.findByRoleId(role.getId());

        for(int i=0;i<dbPermissionList.size();i++){
            for(int j=0;j<permissionList.size();j++){
                if(dbPermissionList.get(i).getId().equals(permissionList.get(j).getId())){
                    permissionList.remove(j);
                    dbPermissionList.remove(i);
                    --i;
                    break;
                }
            }
        }
        System.out.println(dbPermissionList);
        System.out.println(permissionList);
        //需要删除的权限数组
        for(int i=0;i<dbPermissionList.size();i++){
            roleMapper.deleteRolePermission(role.getId(),dbPermissionList.get(i).getId());
        }
        //需要新增的权限数组
        for(int i=0;i<permissionList.size();i++){
            roleMapper.insertRolePermission(role.getId(),permissionList.get(i).getId());
        }
        roleMapper.updateRole(role);
    }

    @Override
    public int deleteRole(Role role) {
        return roleMapper.deleteRole(role);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
    public void checkMenuPermit(Integer roleId,ArrayList newMenusId) throws Exception {
        List<Integer> oldMenusId = menuService.findAllMenuIdByRoleId(roleId);

        for(int i=0;i<oldMenusId.size();i++){
            for(int j=0;j<newMenusId.size();j++){
                if(oldMenusId.get(i) == (int)newMenusId.get(j)){
                    newMenusId.remove(j);
                    oldMenusId.remove(i);
                    --i;
                    break;
                }
            }
        }
        //需要删除的菜单数组
        for(int i=0;i<oldMenusId.size();i++){
            roleMapper.deleteRoleMenu(roleId, oldMenusId.get(i));
        }
        //需要新增的菜单数组
        for(int i=0;i<newMenusId.size();i++){
            roleMapper.insertRoleMenu(roleId, (int)newMenusId.get(i));
        }
    }
}

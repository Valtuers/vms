package com.vms.service;

import com.vms.bean.Permission;
import com.vms.bean.Role;

import java.util.ArrayList;
import java.util.List;

public interface RoleService {

    List<Role> findAll(Role role);

    List<Role> findAllAndUser(Role role);

    List<Role> findByUserId(int user_id, String name);

    void addRoleAndMenu(String name,String remark,ArrayList menu) throws Exception;

    void updateRoleAndPermit(Role role) throws Exception;

    int deleteRole(Role role);

    void checkMenuPermit(Integer roleId, ArrayList newMenusId) throws Exception;
}

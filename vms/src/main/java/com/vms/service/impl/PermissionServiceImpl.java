package com.vms.service.impl;

import com.vms.bean.Permission;
import com.vms.dao.PermissionMapper;
import com.vms.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionMapper permissionMapper;

    @Override
    public List<Permission> findAllPermission(Permission permission) {
        return permissionMapper.findAllWhereUrl(permission);
    }

    @Override
    public int addPermission(Permission permission) {
        return permissionMapper.addPermission(permission);
    }

    @Override
    public int updatePermission(Permission permission) {
        return permissionMapper.updatePermission(permission);
    }

    @Override
    public int deletePermission(int id) {
        return permissionMapper.deletePermission(id);
    }
}

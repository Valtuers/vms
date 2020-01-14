package com.vms.service;

import com.vms.bean.Permission;

import java.util.List;

public interface PermissionService {

    List<Permission> findAllPermission(Permission permission);

    int addPermission(Permission permission);

    int updatePermission(Permission permission);

    int deletePermission(int id);
}

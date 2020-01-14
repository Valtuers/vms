package com.vms.dao;

import com.vms.bean.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PermissionMapper {

    List<Permission> findAll();

    List<Permission> findAllWhereUrl(Permission permission);

    List<Permission> findAllAndRole();

    List<Permission> findByRoleId(int role_id);

    List<Permission> findByUserName(@Param("user_name") String user_name);

    int addPermission(Permission permission);

    int updatePermission(Permission permission);

    int deletePermission(Integer id);
}

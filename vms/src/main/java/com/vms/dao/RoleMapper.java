package com.vms.dao;

import com.vms.bean.Role;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleMapper {

    List<Role> findAll(Role role);

    List<Role> findAllAndUser(Role role);

    List<Role> findByUserId(@Param("user_id") int user_id,@Param("name") String name);

//    @Select("SELECT ur.role_id id,r.name name,r.remark remark FROM user_role ur LEFT JOIN role r ON r.id=ur.role_id WHERE ur.user_id=#{user_id}")
//    @Results(
//        value = {
//            @Result(id = true,property = "id",column = "id"),
//            @Result(property = "name",column = "name"),
//            @Result(property = "remark",column = "remark"),
//            @Result(
//                    property = "permissionList",column = "id",many = @Many(select = "com.vms.dao.PermissionMapper.findByRoleId",fetchType =FetchType.DEFAULT)
//            )
//        }
//    )
    List<Role> findPermissionByUserId(int user_id);

    List<Role> findMenuByUserId(int user_id);

    //创建角色返回主键
    int insertRole(Role role);

    int insertRoleMenu(@Param("roleId") int roleId,@Param("menuId") int menuId);

    int insertUserRole(@Param("userId") int userId,@Param("roleId") int roleId);

    int insertRolePermission(@Param("roleId") int roleId,@Param("permissionId") int permissionId);

    int updateRole(Role role);

    int deleteRole(Role role);

    int deleteRoleMenu(@Param("roleId") int roleId,@Param("menuId") int menuId);

    int deleteRolePermission(@Param("roleId") int roleId,@Param("permissionId") int permissionId);
}

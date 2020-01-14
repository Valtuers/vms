package com.vms.controller;

import com.vms.bean.Permission;
import com.vms.bean.Role;
import com.vms.bean.User;
import com.vms.service.PermissionService;
import com.vms.service.RoleService;
import com.vms.shiro.ShiroService;
import com.vms.utils.JsonData;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    ShiroService shiroService;


    @RequestMapping("/setMenuPermit")
    public JsonData setMenuPermit(@RequestBody Map<String,Object> map){
        try {
            roleService.checkMenuPermit((Integer)map.get("roleId"),(ArrayList) map.get("menusId"));
            return JsonData.success("设置菜单权限成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonData.error("设置菜单权限失败！！！");
        }
    }

    @RequestMapping("/getAllRole")
    public JsonData allRole(@RequestBody Role role){
        List<Role> roles = roleService.findAll(role);

        if(roles != null){
            return JsonData.success(roles);
        }else{
            return JsonData.error("角色获取失败");
        }
    }

    @RequestMapping("/getAllPermission")
    public JsonData allPermission(@RequestBody Permission permission){
        List<Permission> permis = permissionService.findAllPermission(permission);

        if(permis != null){
            return JsonData.success(permis);
        }else{
            return JsonData.error("权限获取失败");
        }
    }

    @RequestMapping("/getAllRoleAndUser")
    public JsonData allRoleAndUser(@RequestBody Role role){
        List<Role> roles = roleService.findAllAndUser(role);

        if(roles != null){
            return JsonData.success(roles);
        }else{
            return JsonData.error("角色获取失败");
        }
    }

    @RequestMapping("/getUserRole")
    public JsonData userRole(@RequestBody Role role){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Role> roles = roleService.findByUserId(user.getId(),role.getName());

        if(roles != null){
            return JsonData.success(roles);
        }else{
            return JsonData.error("角色获取失败");
        }
    }

    @RequestMapping("/addPermission")
    public JsonData addPermission(@RequestBody Permission permission){
        int i = permissionService.addPermission(permission);
        if(i>0){
            shiroService.updatePermission();
            return JsonData.success("新增权限成功！！！");
        }else{
            return JsonData.error("新增权限失败！！！");
        }
    }

    @RequestMapping("/updatePermission")
    public JsonData updatePermission(@RequestBody Permission permission){
        int i = permissionService.updatePermission(permission);
        if(i>0){
            shiroService.updatePermission();
            return JsonData.success("修改权限成功！！！");
        }else{
            return JsonData.error("修改权限失败！！！");
        }
    }

    @RequestMapping("/delPermission")
    public JsonData delPermission(@RequestBody Permission permission){
        int i = permissionService.deletePermission(permission.getId());
        if(i>0){
            shiroService.updatePermission();
            return JsonData.success("删除权限成功！！！");
        }else{
            return JsonData.error("删除权限失败！！！");
        }
    }

    @RequestMapping("/addRoleAndMenu")
    public JsonData addRoleAndMenu(@RequestBody Map<String,Object> map){
        try {
            roleService.addRoleAndMenu((String)map.get("name"),(String)map.get("remark"),(ArrayList) map.get(
                    "menu"));
            shiroService.updatePermission();
            return JsonData.success("新增角色成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonData.error("新增角色失败！！！");
        }
    }

    @RequestMapping("/updateRole")
    public JsonData updateRole(@RequestBody Role role){
        try {
            roleService.updateRoleAndPermit(role);
            shiroService.updatePermission();
            return JsonData.success("修改成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonData.error("修改失败！！！");
        }
    }

    @RequestMapping("/delRole")
    public JsonData delRole(@RequestBody Role role){
        int i = roleService.deleteRole(role);
        if(i>0){
            shiroService.updatePermission();
            return JsonData.success("删除成功！！！");
        }else{
            return JsonData.error("删除失败！！！");
        }
    }
}

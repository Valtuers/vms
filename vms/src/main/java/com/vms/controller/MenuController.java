package com.vms.controller;

import com.vms.bean.Menu;
import com.vms.bean.Role;
import com.vms.bean.User;
import com.vms.service.MenuService;
import com.vms.service.RoleService;
import com.vms.utils.JsonData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @Autowired
    RoleService roleService;

    @RequestMapping("/allMenu")
    public JsonData allMenu(){
        return JsonData.success(menuService.findAllMenu());
    }

    @RequestMapping("/getMenu")
    public JsonData getMenu(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        return JsonData.success(menuService.findAllMenuByUserName(user.getUserName()));
    }

    @RequestMapping("/getMenuId")
    public JsonData getMenuId(@RequestBody Role role){
        return JsonData.success(menuService.findAllMenuIdByRoleId(role.getId()));
    }

    @RequestMapping("/setMenuSort")
    public JsonData setMenuSort(@RequestBody Map<String,List<Menu>> map){
        System.out.println(map.get("menus").toString());
        try {
            menuService.setMenuSort(map);
            return JsonData.success("修改菜单排序成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonData.error("修改菜单排序失败！！！");
        }
    }

    @RequestMapping("/addMenu")
    public JsonData addMenu(@RequestBody Menu menu){
        int res = menuService.addMenu(menu);
        if(res > 0){
            return JsonData.success("新增成功！！！");
        }else{
            return JsonData.error("新增失败！！！");
        }
    }

    @RequestMapping("/updateMenu")
    public JsonData updateMenu(@RequestBody Menu menu){
        int res = menuService.updateMenu(menu);
        System.out.println(res);
        if(res > 0){
            return JsonData.success("修改成功！！！");
        }else{
            return JsonData.error("修改失败！！！");
        }
    }

    @RequestMapping("/delMenu")
    public JsonData delMenu(@RequestBody Menu menu){
        int res = menuService.deleteMenuById(menu.getId());
        System.out.println(res);
        if(res > 0){
            return JsonData.success("删除成功！！！");
        }else{
            return JsonData.error("删除失败！！！");
        }
    }

    @RequestMapping("/delMenuBatch")
    public JsonData delMenuBatch(@RequestBody Map<String,Integer[]> map){
        int length = map.get("id").length;
        int count = 0;
        for (Integer item : map.get("id")) {
            int res = menuService.deleteMenuById(item);
            count += res;
        }
        if(count == length){
            return JsonData.success("删除成功！！！");
        }else{
            return JsonData.error("删除失败！！！");
        }
    }
}

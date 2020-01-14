package com.vms.controller;

import com.github.pagehelper.PageInfo;
import com.vms.bean.Role;
import com.vms.bean.User;
import com.vms.service.MenuService;
import com.vms.service.RoleService;
import com.vms.service.UserService;
import com.vms.utils.JsonData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @Autowired
    RoleService roleService;

    @RequestMapping("/allUser")
    public List<User> allUser(){
        return userService.findAllUser();
    }

    @RequestMapping("/login")
    public JsonData login(@RequestBody User user){
        String userName = user.getUserName();
        String password = user.getPassword();

        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
            subject.login(token);
            Map<String,Object> res = new HashMap<>();
            res.put("menu", menuService.findAllMenuByUserName(userName));
            res.put("token", subject.getSession().getId());
            return JsonData.success(res);
        }catch (UnknownAccountException e){
            //登陆失败:用户名不存在
            return JsonData.error("用户名不存在");
        }catch (IncorrectCredentialsException e){
            //登陆失败:密码错误
            return JsonData.error("密码错误");
        }
    }

    @RequestMapping("/logout")
    public JsonData logout(){
        SecurityUtils.getSubject().logout();
        return JsonData.success("退出成功");
    }

    @RequestMapping("/toLogin")
    public JsonData toLogin(){
        return JsonData.login("需要登录");
    }

    @RequestMapping("/notPermit")
    public JsonData notPermit(){
        return JsonData.permit("需要授权");
    }

    @RequestMapping("/userInfo")
    public JsonData userInfo(){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        user.setPassword(null);
        return JsonData.success(user);
    }

    @RequestMapping("/getAllUserRole")
    public JsonData allUserRole(@RequestBody Role role){
        List<User> users = userService.findAllAndRole();

        if(users != null){
            return JsonData.success(users);
        }else{
            return JsonData.error("用户获取失败");
        }
    }

    @RequestMapping("/getUserByPage")
    public JsonData userByPage(@RequestBody Map<String,Object> where){
        PageInfo<User> userList = userService.findUserByWhere(where);
        if(userList != null){
            return JsonData.success(userList);
        }else{
            return JsonData.error("获取用户失败！！！");
        }
    }

    @RequestMapping("/addUser")
    public JsonData addUser(@RequestBody User user, HttpServletRequest request){
        try {
            userService.addUserAndRole(user,request);
            return JsonData.success("新增用户成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonData.error("新增用户失败！！！");
        }
    }

    @RequestMapping("/delUser")
    public JsonData delUser(@RequestBody User user){
        System.out.println(user);
        int i = userService.deleteUser(user.getId());
        if(i > 0){
            return JsonData.success("删除用户成功！！！");
        } else {
            return JsonData.error("删除用户失败！！！");
        }
    }
}

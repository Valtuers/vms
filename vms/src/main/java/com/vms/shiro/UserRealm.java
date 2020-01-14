package com.vms.shiro;

import com.vms.bean.Permission;
import com.vms.bean.Role;
import com.vms.bean.User;
import com.vms.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("路由授权");

        User s_user = (User) principals.getPrimaryPrincipal();
        User user = userService.findUserDetail(s_user.getUserName());
        List<String> roleList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        System.out.println(user);
        for(Role r:user.getRoleList()){
            roleList.add(Integer.toString(r.getId()));

            for (Permission p: r.getPermissionList()){
                if(p!=null){
                    permissionList.add(Integer.toString(p.getId()));
                }
            }
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roleList);
        simpleAuthorizationInfo.addStringPermissions(permissionList);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("登陆认证");
        String userName = (String) token.getPrincipal();
        User user = userService.findUserDetail(userName);
//        System.out.println(new SimpleHash("md5",user.getPassword(),null,2));
        if(user != null){
            String password = user.getPassword();
            if(password == null || "".equals(password)){
                return null;
            }
            return new SimpleAuthenticationInfo(user,user.getPassword(),this.getClass().getName());
        }
        return null;
    }
}

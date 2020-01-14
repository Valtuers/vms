package com.vms.shiro;

import com.vms.bean.Permission;
import com.vms.dao.PermissionMapper;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {

    @Autowired
    PermissionMapper permissionMapper;

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        //获取所有权限列表
        List<Permission> permissionList = permissionMapper.findAllAndRole();

        System.out.println("权限初始化...");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //必须设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //访问需要登录的接口却没有登录，则调用该接口
        shiroFilterFactoryBean.setLoginUrl("/user/toLogin");

        //登录成功跳转接口
        //shiroFilterFactoryBean.setSuccessUrl("/");

        //没有授权接口
        shiroFilterFactoryBean.setUnauthorizedUrl("/user/notPermit");

        //配置自定义filter
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("permsFilter", new UserRolesFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        /**
         * Shiro内置拦截器，可以实现权限相关的拦截器
         * anon:无需拦截(登陆)可以访问
         * authc:必须验证才可以访问
         * role:该资源必须得到角色权限才可以访问
         * perms:该资源必须得到资源权限才可以访问
         * mapper:如果使用rememberMe功能可以直接访问
         */
        Map<String,String> permitMap = new LinkedHashMap<>();
        //登陆
        permitMap.put("/user/login", "anon");
        //退出登录
        permitMap.put("/user/logout", "anon");
        //静态资源
        permitMap.put("/images/**", "anon");

        //自定义过滤器rolesFilter配置所有权限
        for (int i = 0; i < permissionList.size(); i++) {
            List<String> arr = new ArrayList<>();
            for (int j = 0; j < permissionList.get(i).getRoleList().size(); j++) {
                arr.add(Integer.toString(permissionList.get(i).getRoleList().get(j).getId()));
            }
            permitMap.put(permissionList.get(i).getUrl(),"permsFilter"+arr.toString());
        }
        //必须登录才能访问
        permitMap.put("/**", "authc");
        //编辑权限才能访问
//        permitMap.put("/video/update", "perms[video_update]");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(permitMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        defaultWebSecurityManager.setRealm(userRealm());
        //如果不是前后端分离则不必设置SessionManager
        defaultWebSecurityManager.setSessionManager(sessionManager());
        return defaultWebSecurityManager;
    }

    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    @Bean
    public SessionManager sessionManager() {
        UserSessionManager userSessionManager = new UserSessionManager();

        //session过期时间(毫秒)，默认30分钟
        userSessionManager.setGlobalSessionTimeout(3000000);
        return userSessionManager;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        //使用散列算法(md5)
        hashedCredentialsMatcher.setHashAlgorithmName("md5");

        //散列2次
        hashedCredentialsMatcher.setHashIterations(2);

        return hashedCredentialsMatcher;
    }
}

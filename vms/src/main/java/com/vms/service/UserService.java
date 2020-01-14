package com.vms.service;

import com.github.pagehelper.PageInfo;
import com.vms.bean.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> findAllUser();

    PageInfo<User> findUserByWhere(Map<String,Object> where);

    User findUserDetail(String userName);

    User findUserSimple(String userName);

    List<User> findAllAndRole();

    void addUserAndRole(User user, HttpServletRequest request) throws Exception;

    int deleteUser(Integer id);
}

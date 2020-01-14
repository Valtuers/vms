package com.vms.dao;

import com.vms.bean.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserMapper {

    List<User> findAllUser();

    List<User> findAllUserByWhere(Map<String,Object> where);

    List<User> findAllAndRole();

    User findByUserName(String userName);

    User findSimpleByUserName(String userName);

    int insertUser(User user);

    int delUserById(Integer id);
}

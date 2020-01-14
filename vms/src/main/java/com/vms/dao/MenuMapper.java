package com.vms.dao;

import com.vms.bean.Menu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MenuMapper {

    List<Menu> getAllMenu();

    List<Menu> findByRoleId(int role_id);

    List<Menu> findByUserName(String userName);

    int insertByPid(Menu menu);

    int updateById(Menu menu);

    int updateSortById(Menu menu);

    int deleteById(int id);
}

package com.vms.service;

import com.vms.bean.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {

    List<Menu> findAllMenu();

    List<Integer> findAllMenuIdByRoleId(Integer role_id);

    List<Menu> findAllMenuByUserName(String userName);

    int addMenu(Menu menu);

    int updateMenu(Menu menu);

    int deleteMenuById(int id);

    void setMenuSort(Map<String,List<Menu>> map) throws Exception;
}

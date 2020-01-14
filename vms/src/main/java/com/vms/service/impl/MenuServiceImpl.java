package com.vms.service.impl;

import com.vms.bean.Menu;
import com.vms.dao.MenuMapper;
import com.vms.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuMapper menuMapper;


    @Override
    public List<Menu> findAllMenu() {
        return treeMenu(menuMapper.getAllMenu(),new Menu(),0).getChildren();
    }

    @Override
    public List<Integer> findAllMenuIdByRoleId(Integer role_id) {
        List<Menu> menus = menuMapper.findByRoleId(role_id);
        List<Integer> menuId = new ArrayList<>();
        for (Menu item : menus) {
            menuId.add(item.getId());
        }
        return menuId;
    }

    @Override
    public List<Menu> findAllMenuByUserName(String userName) {
        return treeMenu(menuMapper.findByUserName(userName),new Menu(),0).getChildren();
    }

    @Override
    public int addMenu(Menu menu) {
        String path = menu.getPath().replaceAll("^(/)","");
        menu.setName(path);
        menu.setUrl(path);
        menu.setSort(999);
        return menuMapper.insertByPid(menu);
    }

    @Override
    public int updateMenu(Menu menu) {
        String path = menu.getPath().replaceAll("^(/)","");
        menu.setName(path);
        menu.setUrl(path);
        return menuMapper.updateById(menu);
    }

    @Override
    public int deleteMenuById(int id) {
        return menuMapper.deleteById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
    public void setMenuSort(Map<String, List<Menu>> map) throws Exception {
        List<Menu> menuList = map.get("menus");
        for(int i=0;i<menuList.size();i++){
            menuList.get(i).setPid(0);
            if(menuList.get(i).getChildren() != null){
                for(int j=0;j<menuList.get(i).getChildren().size();j++){
                    menuList.get(i).getChildren().get(j).setPid(menuList.get(i).getId());
                    menuMapper.updateSortById(menuList.get(i).getChildren().get(j));
                }
            }
            menuMapper.updateSortById(menuList.get(i));
        }
    }

    private static Menu treeMenu(List<Menu> data,Menu nowData, int pid){
        for(int i=0;i<data.size();i++) {
            if (data.get(i) != null && data.get(i).getPid() == pid) {
                if (nowData.getChildren() == null)
                    nowData.setChildren(new ArrayList<>());
                nowData.addChildren(treeMenu(data, data.get(i), data.get(i).getId()));
            }
        }
        return nowData;
    }
}

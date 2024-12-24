package com.keshe.service;

import com.keshe.entity.SysMenu;
import com.keshe.mapper.MenuMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    
    @Resource
    private MenuMapper menuMapper;
    
    // 获取所有顶级菜单
    public List<SysMenu> getTopMenus() {
        return menuMapper.getAllMenus().stream()
                .filter(menu -> menu.getParent_id() == 0)
                .collect(Collectors.toList());
    }

    public SysMenu getMenuById(Long id) {
        return menuMapper.getMenuById(id);
    }
    
    public void insertMenu(SysMenu menu) {
        menuMapper.insertMenu(menu);
    }
    
    public void updateMenu(SysMenu menu) {
        menu.setUpdated(LocalDateTime.now());
        menuMapper.updateMenu(menu);
    }
    
    public void deleteMenu(Long id) {
        menuMapper.deleteMenu(id);
    }
    
    public void updateStatus(Long id, Integer status) {
        SysMenu menu = menuMapper.getMenuById(id);
        if (menu != null) {
            menu.setStatus(status);
            menu.setUpdated(LocalDateTime.now());
            menuMapper.updateMenu(menu);
        }
    }

    public List<Long> getRoleMenus(Long menuId) {
        return menuMapper.getRoleMenus(menuId);
    }

    public void insertRoleMenu(Long roleId, Long menuId) {
        menuMapper.insertRoleMenu(roleId, menuId);
    }

    public void deleteRoleMenu(Long roleId, Long menuId) {
        menuMapper.deleteRoleMenu(roleId, menuId);
    }

    public List<SysMenu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}

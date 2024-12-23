package com.keshe.controller;

import com.keshe.entity.RestBean;
import com.keshe.entity.SysMenu;
import com.keshe.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/top")
    public RestBean<Map<String, Object>> getTopMenus() {
        Map<String, Object> data = new HashMap<>();
        data.put("menus", menuService.getTopMenus());
        return RestBean.success(data);
    }

    @PostMapping("/add")
    public RestBean<Map<String, Object>> addMenu(@RequestBody SysMenu menu) {
        Map<String, Object> data = new HashMap<>();
        menuService.insertMenu(menu);
        data.put("menu", menu);
        return RestBean.success(data);
    }

    @PutMapping("/update")
    public RestBean<Map<String, Object>> updateMenu(@RequestBody SysMenu menu) {
        Map<String, Object> data = new HashMap<>();
        menuService.updateMenu(menu);
        data.put("menu", menu);
        return RestBean.success(data);
    }

    @DeleteMapping("/{id}")
    public RestBean<Map<String, Object>> deleteMenu(@PathVariable Long id) {
        Map<String, Object> data = new HashMap<>();
        menuService.deleteMenu(id);
        data.put("id", id);
        return RestBean.success(data);
    }

    @PutMapping("/status")
    public RestBean<Map<String, Object>> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.parseLong(params.get("id").toString());
        Integer status = Integer.parseInt(params.get("status").toString());
        
        Map<String, Object> data = new HashMap<>();
        menuService.updateStatus(id, status);
        data.put("id", id);
        data.put("status", status);
        return RestBean.success(data);
    }

    @GetMapping("/all")
    public RestBean<Map<String, Object>> getAllMenus() {
        Map<String, Object> data = new HashMap<>();
        data.put("menus", menuService.getAllMenus());
        return RestBean.success(data);
    }

    @GetMapping("/role-menus")
    public RestBean<Map<String, Object>> getRoleMenus(@RequestParam Long menuId) {
        Map<String, Object> data = new HashMap<>();
        data.put("menus", menuService.getRoleMenus(menuId));
        return RestBean.success(data);
    }


    @GetMapping("/menubyid")
    public RestBean<Map<String, Object>> getMenuById(@RequestParam Long id) {
        Map<String, Object> data = new HashMap<>();
        data.put("menu", menuService.getMenuById(id));
        return RestBean.success(data);
    }

    @PostMapping("/role-menu/add")
    public RestBean<Map<String, Object>> addRoleMenu(@RequestBody Map<String, Object> params) {
        Long roleId = Long.parseLong(params.get("roleId").toString());
        Long menuId = Long.parseLong(params.get("menuId").toString());
        
        Map<String, Object> data = new HashMap<>();
        menuService.insertRoleMenu(roleId, menuId);
        data.put("roleId", roleId);
        data.put("menuId", menuId);
        return RestBean.success(data);
    }

    @PostMapping("/role-menu/delete")
    public RestBean<Map<String, Object>> deleteRoleMenu(@RequestBody Map<String, Object> params) {
        Long roleId = Long.parseLong(params.get("roleId").toString());
        Long menuId = Long.parseLong(params.get("menuId").toString());

        Map<String, Object> data = new HashMap<>();
        menuService.deleteRoleMenu(roleId, menuId);
        data.put("roleId", roleId);
        data.put("menuId", menuId);
        return RestBean.success(data);
    }

} 
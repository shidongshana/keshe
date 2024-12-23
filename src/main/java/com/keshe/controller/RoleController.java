package com.keshe.controller;

import com.keshe.entity.*;
import com.keshe.service.RoleService;
import com.keshe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/role")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
     private UserService userService;

    @GetMapping("/info")
    public RestBean<Map<String, Object>> getRoleInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser user = userService.getUserByUsername(username);
        user.setPassword(null);
        Long id = user.getId();
        Long roleid = roleService.getUserRoles(id).getRole_id();
        String roleinfo = roleService.getRoleById(roleid).toString();
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("role", roleinfo);
        return RestBean.success(data);
    }

    @GetMapping("/menu-info")
    public RestBean <Map<String, Object>> getMenuInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser user = userService.getUserByUsername(username);
        user.setPassword(null);
        Long id = user.getId();
        Long roleid = roleService.getUserRoles(id).getRole_id();
        List<SysRoleMenu> menus = roleService.getRoleMenus(roleid);
        Map <String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("menus", menus);
        return RestBean.success(data);
    }

    @GetMapping("/all")
    public RestBean<Map<String,Object>> findAll() {
        Map<String,Object> data = new HashMap<>();
        data.put("roles", roleService.findAll());
        return RestBean.success(data);
    }

    @PostMapping("/add")
    public RestBean<Map<String,Object>> addRole(SysRole Role) {
        Map<String,Object> data = new HashMap<>();
        data.put("role", roleService.insert(Role));
        return RestBean.success(data);
    }

    @DeleteMapping("/{id}")
    public RestBean<Map<String,Object>> deleteRole(@PathVariable Long id) {
        Map<String,Object> data = new HashMap<>();
        data.put("role", roleService.deleteById(id));
        return RestBean.success(data);
    }

    @PutMapping("/update")
    public RestBean<Map<String,Object>> updateRole(@RequestBody SysRole role) {
        Map<String,Object> data = new HashMap<>();
        data.put("role", roleService.update(role));
        return RestBean.success(data);
    }
    


}

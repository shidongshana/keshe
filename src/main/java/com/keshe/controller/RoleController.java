package com.keshe.controller;

import com.keshe.entity.RestBean;
import com.keshe.entity.SysRoleMenu;
import com.keshe.entity.SysUser;
import com.keshe.entity.SysUserRole;
import com.keshe.service.RoleService;
import com.keshe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

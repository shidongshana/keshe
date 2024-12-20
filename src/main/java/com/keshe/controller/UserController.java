package com.keshe.controller;

import com.keshe.entity.RestBean;
import com.keshe.entity.SysUser;
import com.keshe.entity.SysUserRole;
import com.keshe.service.UserService;
import com.keshe.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService RoleService;

    @Operation(summary = "获取当前用户名",
            description = "获取当前登录用户的用户名")
    @ApiResponse(responseCode = "200", description = "获取成功",
            content = @Content(schema = @Schema(implementation = RestBean.class)))
    @GetMapping("/name")
    public RestBean<String> username() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return RestBean.success(username);
    }

    @Operation(summary = "获取用户信息",
            description = "获取当前登录用户的详细信息")
    @ApiResponse(responseCode = "200", description = "获取成功",
            content = @Content(schema = @Schema(implementation = RestBean.class)))
    @GetMapping("/info")
    public RestBean<Map<String, Object>> getUserInfo() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    SysUser user = userService.getUserByUsername(username);
    user.setPassword(null);  // 出于安全考虑，不返回密码
    // 获取权限
    Long id = user.getId();
    Long roleid = RoleService.getUserRoles(id).getRole_id();
    String roleinfo = RoleService.getRoleById(roleid).toString();
    Map<String, Object> data = new HashMap<>();
    data.put("user", user);
    data.put("role", roleinfo);

    return RestBean.success(data);
}

    @Operation(summary = "用户注册",
            description = "新用户注册接口")
    @ApiResponse(responseCode = "200", description = "注册成功",
            content = @Content(schema = @Schema(implementation = RestBean.class)))
    @PostMapping("/register")
    public RestBean<String> register(
            @Parameter(description = "用户名", required = true) @RequestParam String username,
            @Parameter(description = "密码", required = true) @RequestParam String password,
            @Parameter(description = "电子邮箱", required = true) @RequestParam String email,
            @Parameter(description = "城市", required = true) @RequestParam String city) {
        userService.register(username, password, email, city);
        return RestBean.success("注册成功");
    }


    @Operation(summary = "用户角色更新",
            description = "用户角色更新接口")
    @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = RestBean.class)))
    @PostMapping("/updateRole")
    public RestBean<String> updateRole(
            @Parameter(description = "用户ID", required = true) @RequestParam Long userId,
            @Parameter(description = "角色ID", required = true) @RequestParam Long roleId) {
        //判断操作者是否为管理员
        String role = userService.getUserRole(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!role.contains("1")) {
            return RestBean.failure(403, role + "无权限");
        }
        SysUserRole userRole = new SysUserRole();
        userRole.setUser_id(userId);
        userRole.setRole_id(roleId);
        userService.updateUserRole(userRole);
        return RestBean.success("更新成功");
    }
}

package com.keshe.controller;

import com.keshe.entity.RestBean;
import com.keshe.entity.SysUser;
import com.keshe.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

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
    public RestBean<SysUser> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser user = userService.getUserByUsername(username);
        user.setPassword(null);  // 出于安全考虑，不返回密码
        return RestBean.success(user);
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
}

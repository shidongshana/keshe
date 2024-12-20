package com.keshe.controller;

import com.keshe.entity.RestBean;
import com.keshe.entity.SysUser;
import com.keshe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/name")
    public RestBean<String> username() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return RestBean.success(username);
    }

    @GetMapping("/info")
    public RestBean<SysUser> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser user = userService.getUserByUsername(username);
        user.setPassword(null);  // 出于安全考虑，不返回密码
        return RestBean.success(user);
    }

    @PostMapping("/register")
    public RestBean<String> register(@RequestParam String username, 
                                   @RequestParam String password,
                                   @RequestParam String email,
                                   @RequestParam String city) {
        userService.register(username, password, email, city);
        return RestBean.success("注册成功");
    }


}

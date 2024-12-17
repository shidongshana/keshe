package com.keshe.controller;

import com.keshe.entity.RestBean;
import com.keshe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController   //为了方便，我们一律使用RestController，这样每个请求默认都返回JSON对象
@RequestMapping("/api/user")   //用户相关的接口，路径可以设置为/api/user/xxxx
public class UserController {


    @Autowired
    private UserService userService;
    @GetMapping("/name")
    public RestBean<String> username() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return RestBean.success(user.getUsername());
    }

}

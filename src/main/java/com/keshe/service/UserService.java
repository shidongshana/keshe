package com.keshe.service;

import com.keshe.mapper.UserMapper;
import com.keshe.model.UserModel;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("登录失败，用户名或密码错误！");
        }

        return User.withUsername(user.getUsername()).password(passwordEncoder.encode(user.getPassword())).authorities(user.getAuthorities()).build();
    }
    public UserModel register(String username, String password, int role) {
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        try {
            userMapper.insertUser(user);
        } catch (Exception e) {
            throw new RuntimeException("注册失败，用户名已存在！");
        }
        return user;

    }


}

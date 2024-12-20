package com.keshe.service;

import com.keshe.mapper.UserMapper;
import com.keshe.entity.User;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("登录失败，用户名或密码错误！");
        }
        
        // 更新最后登录时间
        user.setLastLogin(new Date());
        userMapper.updateLoginTime(user);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }

    public User getUserByUsername(String username) {
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    public User register(String username, String password, String email, String city) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setCity(city);
        user.setCreated(new Date());
        user.setStatus(1);  // 默认激活状态
        user.setAvatar("https://api.multiavatar.com/" + username + ".png");  // 默认头像
        
        try {
            userMapper.insertUser(user);
        } catch (Exception e) {
            throw new RuntimeException("注册失败，用户名已存在！");
        }
        return user;
    }
}

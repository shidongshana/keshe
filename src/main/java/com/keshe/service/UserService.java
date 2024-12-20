package com.keshe.service;

import com.keshe.mapper.UserMapper;
import com.keshe.entity.SysUser;
import com.keshe.entity.SysUserRole;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("登录失败，用户名或密码错误！");
        }
        
        // 获取用户角色和权限
        List<String> roles = userMapper.getUserRoles(user.getId());
        List<String> permissions = userMapper.getUserPermissions(user.getId());
        
        // 合并角色和权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        
        // 更新最后登录时间
        user.setLastLogin(LocalDateTime.now());
        userMapper.updateLoginTime(user);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    public String getUserRole(String username) {
        SysUser user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<String> roles = userMapper.getUserRoles(user.getId());

        return roles.stream().collect(Collectors.joining(","));
    }

    public SysUser getUserByUsername(String username) {
        SysUser user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    public void updateUserRole(SysUserRole userRole) {
        if (userMapper.getUserById(userRole.getUserId()) == null) {
           userMapper.insertUserRole(userRole);
        }
        userMapper.updateUserrole(userRole);
    }
    public SysUser register(String username, String password, String email, String city) {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setCity(city);
        user.setCreated(LocalDateTime.now());
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

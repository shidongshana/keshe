package com.keshe.service;

import com.keshe.entity.Test;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<SysUser> AllUser() {
        return userMapper.AllUser();
    }
    public SysUser getUserByUsername(String username) {
        SysUser user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    public void updateUserRole(SysUserRole userRole) {
        if (userMapper.getUserById(userRole.getRole_id()) == null) {
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

    public void updateUser(SysUser user) {
        // 设置更新时间
        user.setUpdated(LocalDateTime.now());
        
        // 如果密码字段不为空，则需要加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        System.out.println(user);
        int result = userMapper.updateUser(user);
        System.out.println(result);
        if (result == 0) {
            throw new RuntimeException("更新失败，用户不存在");
        }
    }

    public void updateUserStatus(Long userId, Integer status) {
        if (status != 0 && status != 1) {
            throw new IllegalArgumentException("状态值无效");
        }
        
        int result = userMapper.updateUserStatus(userId, status, LocalDateTime.now());
        if (result == 0) {
            throw new RuntimeException("更新失败，用户不存在");
        }
    }

    public Map<String, Object> getUsersByPage(int page, int pageSize) {
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        
        // 获取分页数据
        List<SysUser> users = userMapper.getUsersByPage(offset, pageSize);
        
        // 获取总记录数
        int total = userMapper.getTotalUsers();
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", users);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        
        return result;
    }

    public String updateAvatar(String name, String avatar) {
        SysUser user = userMapper.getUserByUsername(name);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        user.setAvatar(avatar);
        userMapper.updateUser(user);
        return avatar;
    }

    public int getUserStatus(String username) {
        return userMapper.getUserStatus(username);
    }


}

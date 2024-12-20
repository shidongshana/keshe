package com.keshe.service;

import com.keshe.entity.SysRole;
import com.keshe.entity.SysRoleMenu;
import com.keshe.entity.SysUserRole;
import com.keshe.mapper.RoleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;




    public SysRole getRoleById(Long id) {
        return roleMapper.getRoleById(id);
    }

    public SysUserRole getUserRoles(Long userId) {
        return roleMapper.getUserRoles(userId);
    }

    public List<SysRoleMenu> getRoleMenus(Long roleId) {
        return roleMapper.getRoleMenus(roleId);
    }
}

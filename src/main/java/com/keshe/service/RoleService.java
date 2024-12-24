package com.keshe.service;

import com.keshe.entity.SysRole;
import com.keshe.entity.SysRoleMenu;
import com.keshe.entity.SysUserRole;
import com.keshe.mapper.RoleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
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


    public List<SysRole> findAll() {
        return roleMapper.findAll();
    }

    public int insert(SysRole role) {
        LocalDateTime now = LocalDateTime.now();
        role.setCreated(now);
        return roleMapper.insert(role);
    }



    public int deleteById(Long id) {
        return roleMapper.deleteById(id);
    }

    public int update(SysRole role) {
        return roleMapper.update(role);
    }

    public SysUserRole updateRole(SysUserRole userRole) {
        roleMapper.updateRole(userRole);
        return userRole;
    }
}

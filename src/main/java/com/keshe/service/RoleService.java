package com.keshe.service;

import com.keshe.entity.SysRole;
import com.keshe.mapper.RoleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;


    public SysRole getRoleById(Long id) {
        return roleMapper.getRoleById(id);
    }
}

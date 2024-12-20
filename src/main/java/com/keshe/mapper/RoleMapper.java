package com.keshe.mapper;

import com.keshe.entity.SysRole;
import com.keshe.entity.SysRoleMenu;
import com.keshe.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {
    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole getRoleById(Long id);

    @Select("SELECT * FROM sys_user_role WHERE user_id = #{userId}")
    SysUserRole getUserRoles(Long userId);

    @Select("SELECT * FROM sys_role_menu WHERE role_id = #{roleId}")
    List<SysRoleMenu> getRoleMenus(Long roleId);

}

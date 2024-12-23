package com.keshe.mapper;

import com.keshe.entity.SysRole;
import com.keshe.entity.SysRoleMenu;
import com.keshe.entity.SysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RoleMapper {
    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole getRoleById(Long id);

    @Select("SELECT * FROM sys_user_role WHERE user_id = #{userId}")
    SysUserRole getUserRoles(Long userId);

    @Select("SELECT * FROM sys_role_menu WHERE role_id = #{roleId}")
    List<SysRoleMenu> getRoleMenus(Long roleId);

    @Select("SELECT * FROM sys_role")
    List<SysRole> findAll();

    @Insert("INSERT INTO sys_role (name, code,remark,created) VALUES (#{name}, #{code}, #{remark}, #{created})")
    int insert(SysRole role);

    @Delete("DELETE FROM sys_role WHERE id = #{id}")
    int deleteById(Long id);

    @Update("UPDATE sys_role SET name = #{name}, code = #{code}, remark = #{remark} WHERE id = #{id}")
    int update(SysRole role);

}

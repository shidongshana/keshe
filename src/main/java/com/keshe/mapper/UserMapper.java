package com.keshe.mapper;

import com.keshe.entity.SysUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser getUserByUsername(String username);
    
    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    SysUser getUserById(Long id);
    
    @Insert("INSERT INTO sys_user(username, password, avatar, email, city, created, status) " +
            "VALUES(#{username}, #{password}, #{avatar}, #{email}, #{city}, #{created}, #{status})")
    void insertUser(SysUser user);
    
    @Update("UPDATE sys_user SET last_login = #{lastLogin} WHERE id = #{id}")
    void updateLoginTime(SysUser user);

    @Select("SELECT r.* FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> getUserRoles(Long userId);

    @Select("SELECT m.perms FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "LEFT JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.perms IS NOT NULL")
    List<String> getUserPermissions(Long userId);
}

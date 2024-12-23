package com.keshe.mapper;

import com.keshe.entity.SysUser;
import com.keshe.entity.SysUserRole;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
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

    @Update("UPDATE sys_user_role SET role_id = #{roleId} WHERE user_id = #{userId}")
    void  updateUserrole(SysUserRole userrole);

    @Insert("INSERT INTO sys_user_role(user_id, role_id) VALUES(#{userId}, #{roleId})")
    void insertUserRole(SysUserRole userRole);
    
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


    @Select("SELECT * FROM sys_user")
    List<SysUser> AllUser();

    @Update("UPDATE sys_user SET username = #{username} ,email = #{email}, " +
            "city = #{city}, status = #{status},avatar = #{avatar}, updated = #{updated} " +
            "WHERE id = #{id}")
    int updateUser(SysUser user);

    @Update("UPDATE sys_user SET status = #{status}, updated = #{updated} " +
            "WHERE id = #{userId}")
    int updateUserStatus(@Param("userId") Long userId, 
                        @Param("status") Integer status, 
                        @Param("updated") LocalDateTime updated);

    @Select("SELECT * FROM sys_user LIMIT #{offset}, #{pageSize}")
    List<SysUser> getUsersByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM sys_user")
    int getTotalUsers();
}

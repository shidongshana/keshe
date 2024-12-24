package com.keshe.mapper;

import com.keshe.entity.SysMenu;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MenuMapper {

    @Select("SELECT * FROM sys_menu WHERE id = #{id}")
    SysMenu getMenuById(Long id);

    @Select("SELECT * FROM sys_menu")
    List<SysMenu> getAllMenus();

    @Insert("INSERT INTO sys_menu(parent_id, name, path, perms, component, type, icon, orderNum, created, updated, status) " +
            "VALUES(#{parent_id}, #{name}, #{path}, #{perms}, #{component}, #{type}, #{icon}, #{orderNum}, #{created}, #{updated}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertMenu(SysMenu menu);

    @Update("UPDATE sys_menu SET " +
            "parent_id=#{parent_id}, " +
            "name=#{name}, " +
            "path=#{path}, " +
            "perms=#{perms}, " +
            "component=#{component}, " +
            "type=#{type}, " +
            "icon=#{icon}, " +
            "orderNum=#{orderNum}, " +
            "status=#{status}, " +
            "updated=#{updated} " +
            "WHERE id=#{id}")
    void updateMenu(SysMenu menu);

    @Delete("DELETE FROM sys_menu WHERE id = #{id}")
    void deleteMenu(Long id);

    @Select("SELECT role_id FROM sys_role_menu WHERE menu_id = #{menuId}")
    List<Long> getRoleMenus(Long menuId);

    @Select("SELECT menu_id FROM sys_role_menu WHERE role_id = #{roleId}")
    List<Long> getRoleMenusByRoleId(Long roleId);

    @Insert("INSERT INTO sys_role_menu(role_id, menu_id) VALUES(#{roleId}, #{menuId})")
    void insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    @Delete("DELETE FROM sys_role_menu WHERE role_id = #{roleId} AND menu_id = #{menuId}")
    void deleteRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    @Update("UPDATE sys_menu SET status = #{status}, updated = #{updated} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updated") LocalDateTime updated);
}
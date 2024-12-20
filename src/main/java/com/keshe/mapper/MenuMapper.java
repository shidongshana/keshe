package com.keshe.mapper;

import com.keshe.entity.SysMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MenuMapper {

    @Select("SELECT * FROM sys_menu WHERE id = #{id}")
    SysMenu getMenuById(Long id);

    @Select("SELECT * FROM sys_menu")
    List<SysMenu> getAllMenus();

    @Insert("INSERT INTO sys_menu(parent_id, name, path, perms, component, type, icon, orderNum, created, updated, status) " +
            "VALUES(#{parentId}, #{name}, #{path}, #{perms}, #{component}, #{type}, #{icon}, #{orderNum}, #{created}, #{updated}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertMenu(SysMenu menu);

    @Update("UPDATE sys_menu SET parent_id=#{parentId}, name=#{name}, path=#{path}, perms=#{perms}, component=#{component}, type=#{type}, icon=#{icon}, orderNum=#{orderNum}, created=#{created}, updated=#{updated}, status=#{status} WHERE id=#{id}")
    void updateMenu(SysMenu menu);

    @Delete("DELETE FROM sys_menu WHERE id = #{id}")
    void deleteMenu(Long id);
}
package com.keshe.mapper;

import com.keshe.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper {
    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole getRoleById(Long id);

}

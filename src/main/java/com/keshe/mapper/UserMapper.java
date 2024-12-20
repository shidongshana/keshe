package com.keshe.mapper;

import com.keshe.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    User getUserByUsername(String username);
    
    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    User getUserById(int id);
    
    @Insert("INSERT INTO sys_user(username, password, avatar, email, city, created, status) " +
            "VALUES(#{username}, #{password}, #{avatar}, #{email}, #{city}, #{created}, #{status})")
    void insertUser(User user);
    
    @Update("UPDATE sys_user SET last_login = #{lastLogin} WHERE id = #{id}")
    void updateLoginTime(User user);
}

package com.keshe.mapper;

import com.keshe.model.UserModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
@Select("SELECT * FROM user WHERE username = #{username} ")
    UserModel getUserByUsername(String username);
@Select("SELECT * FROM user WHERE id = #{id}")
    UserModel getUserById(int id);
}

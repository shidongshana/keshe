package com.keshe.mapper;

import com.keshe.entity.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BrandMapper {

    @Select("SELECT * FROM brand WHERE id = #{id}")
    Brand findById(Long id);

    @Select("SELECT * FROM brand")
    List<Brand> findAll();

    @Insert("INSERT INTO brand(name, logo, description, status, created, updated) " +
            "VALUES(#{name}, #{logo}, #{description}, #{status}, #{created}, #{updated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Brand brand);

    @Update("UPDATE brand SET name=#{name}, logo=#{logo}, description=#{description}, " +
            "status=#{status}, updated=#{updated} WHERE id=#{id}")
    void update(Brand brand);

    @Delete("DELETE FROM brand WHERE id = #{id}")
    void delete(Long id);

    @Select("SELECT * FROM brand LIMIT #{offset}, #{limit}")
    List<Brand> findPage(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM brand")
    long count();

    @Select("SELECT * FROM brand WHERE name LIKE CONCAT('%', #{name}, '%') LIMIT #{offset}, #{limit}")
    List<Brand> findByNamePage(@Param("name") String name, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM brand WHERE name LIKE CONCAT('%', #{name}, '%')")
    long countByName(String name);
}
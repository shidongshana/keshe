package com.keshe.mapper;

import com.keshe.entity.Type;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TypeMapper {

    @Select("SELECT * FROM type WHERE id = #{id}")
    Type findById(Long id);

    @Select("SELECT * FROM type")
    List<Type> findAll();

    @Insert("INSERT INTO type (name, category_id, description, status, created, updated) " +
            "VALUES (#{name}, #{category_id}, #{description}, #{status}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Type type);

    @Update("UPDATE type SET name = #{name}, category_id = #{category_id}, " +
            "description = #{description}, status = #{status}, updated = now() " +
            "WHERE id = #{id}")
    int update(Type type);

    @Delete("DELETE FROM type WHERE id = #{id}")
    int deleteById(Long id);

    @Select({
        "<script>",
        "SELECT * FROM type",
        "<if test='sortBy != null and sortBy != \"\"'>",
        "  ORDER BY ${sortBy} ${sortOrder}",
        "</if>",
        "LIMIT #{offset}, #{pageSize}",
        "</script>"
    })
    List<Type> findByPage(
        @Param("offset") int offset, 
        @Param("pageSize") int pageSize,
        @Param("sortBy") String sortBy,
        @Param("sortOrder") String sortOrder
    );

    @Select("SELECT COUNT(*) FROM type")
    long count();

    @Select("SELECT * FROM type WHERE name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR description LIKE CONCAT('%', #{keyword}, '%')")
    List<Type> search(@Param("keyword") String keyword);

    @Select("SELECT * FROM type WHERE category_id = #{categoryId}")
    List<Type> findByCategoryId(Long categoryId);
}
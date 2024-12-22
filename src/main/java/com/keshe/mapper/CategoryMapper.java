package com.keshe.mapper;

import com.keshe.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    
    // 插入新分类
    @Insert("INSERT INTO category (name, parent_id, description,  created, updated) " +
            "VALUES (#{name}, #{parent_id}, #{description},  now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);

    // 根据ID删除分类
    @Delete("DELETE FROM category WHERE id = #{id}")
    int deleteById(Long id);

    // 更新分类信息
    @Update("UPDATE category SET name = #{name}, parent_id = #{parent_id}, " +
            "description = #{description}, status = #{status}, updated = now() " +
            "WHERE id = #{id}")
    int update(Category category);

    // 根据ID查询分类
    @Select("SELECT * FROM category WHERE id = #{id}")
    Category findById(Long id);

    // 查询所有分类
    @Select("SELECT * FROM category")
    List<Category> findAll();

    // 分页查询
    @Select({
        "<script>",
        "SELECT * FROM category",
        "<if test='sortBy != null and sortBy != \"\"'>",
        "  ORDER BY ${sortBy} ${sortOrder}",
        "</if>",
        "LIMIT #{offset}, #{pageSize}",
        "</script>"
    })
    List<Category> findByPage(
        @Param("offset") int offset, 
        @Param("pageSize") int pageSize,
        @Param("sortBy") String sortBy,
        @Param("sortOrder") String sortOrder
    );

    // 统计总记录数
    @Select("SELECT COUNT(*) FROM category")
    long count();

    // 模糊搜索（按名称和描述）
    @Select("SELECT * FROM category WHERE name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR description LIKE CONCAT('%', #{keyword}, '%')")
    List<Category> search(@Param("keyword") String keyword);

    // 根据父ID查询子分类
    @Select("SELECT * FROM category WHERE parent_id = #{parentId}")
    List<Category> findByParentId(Long parentId);
}

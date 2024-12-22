package com.keshe.mapper;

import com.keshe.entity.Product;
import com.keshe.entity.ProductImage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product findById(Long id);

    @Select("SELECT * FROM product")
    List<Product> findAll();

    @Insert("INSERT INTO product(name, category_id, type_id, brand_id, price, stock, description, status, created, updated) " +
            "VALUES(#{name}, #{category_id}, #{type_id}, #{brand_id}, #{price}, #{stock}, #{description}, #{status}, #{created}, #{updated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Product product);

    @Update("UPDATE product SET name=#{name}, category_id=#{category_id}, type_id=#{type_id}, brand_id=#{brand_id}, price=#{price}, stock=#{stock}, description=#{description}, status=#{status}, created=#{created}, updated=#{updated} WHERE id=#{id}")
    void update(Product product);

   @Update("UPDATE product SET status=0 WHERE id=#{id}")
   void delete(Long id);


    @Select("SELECT * FROM product WHERE name LIKE CONCAT('%',#{name},'%')")
    List<Product> findByName(String name);


    @Select("SELECT * FROM product_image WHERE product_id = #{product_id}")
    List<ProductImage> findImages(Long product_id);

    @Select("SELECT * FROM product LIMIT #{offset}, #{limit}")
    List<Product> findPage(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM product")
    long count();

    @Select("SELECT * FROM product WHERE name LIKE CONCAT('%',#{name},'%') LIMIT #{offset}, #{limit}")
    List<Product> findByNamePage(@Param("name") String name, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM product WHERE name LIKE CONCAT('%',#{name},'%')")
    long countByName(String name);
}
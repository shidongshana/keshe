package com.keshe.service;



import com.keshe.entity.Product;
import com.keshe.entity.ProductImage;
import com.keshe.entity.PageResult;
import com.keshe.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public Product findById(Long id) {
        return productMapper.findById(id);
    }

    public List<Product> findByName(String name) {
        return productMapper.findByName(name);
    }

    public List<Product> findAll() {
        return productMapper.findAll();
    }

    public void insert(Product product) {
        product.setCreated(LocalDateTime.now());
        productMapper.insert(product);
    }

    public void update(Product product) {
        product.setUpdated(LocalDateTime.now());
        productMapper.update(product);
    }

    public void delete(Long id) {
        productMapper.delete(id);
    }

    public List<ProductImage> findImages(Long product_id) {
        return productMapper.findImages(product_id);
    }

    public void updateImage(ProductImage productImage) {
        productMapper.updateImage(productImage);
    }


    public PageResult<Product> findPage(int page, int limit) {
        int offset = (page - 1) * limit;
        System.out.println("Executing query with offset: " + offset + ", limit: " + limit);
        List<Product> products = productMapper.findPage(offset, limit);
        long total = productMapper.count();
        System.out.println("Total records: " + total);
        return new PageResult<>(products, total);
    }

    public PageResult<Product> findByNamePage(String name, int page, int limit) {
        int offset = (page - 1) * limit;
        List<Product> products = productMapper.findByNamePage(name, offset, limit);
        long total = productMapper.countByName(name);
        return new PageResult<>(products, total);
    }

    public long findMaxId() {
        return productMapper.findMaxId();
    }

    public void insertImage(ProductImage productImage) {
        productMapper.insertImage(productImage);
    }
}
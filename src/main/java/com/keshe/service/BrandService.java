package com.keshe.service;

import com.keshe.entity.Brand;
import com.keshe.entity.PageResult;
import com.keshe.mapper.BrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public Brand findById(Long id) {
        return brandMapper.findById(id);
    }

    public List<Brand> findAll() {
        return brandMapper.findAll();
    }

    public void insert(Brand brand) {
        brand.setCreated(LocalDateTime.now());
        brandMapper.insert(brand);
    }

    public void update(Brand brand) {
        brand.setUpdated(LocalDateTime.now());
        brandMapper.update(brand);
    }

    public void delete(Long id) {
        brandMapper.delete(id);
    }

    public PageResult<Brand> findPage(int page, int limit) {
        int offset = (page - 1) * limit;
        List<Brand> brands = brandMapper.findPage(offset, limit);
        long total = brandMapper.count();
        return new PageResult<>(brands, total);
    }

    public PageResult<Brand> findByNamePage(String name, int page, int limit) {
        int offset = (page - 1) * limit;
        List<Brand> brands = brandMapper.findByNamePage(name, offset, limit);
        long total = brandMapper.countByName(name);
        return new PageResult<>(brands, total);
    }
} 
package com.keshe.service;

import com.keshe.entity.Category;
import com.keshe.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional
    public Category create(Category category) {
        categoryMapper.insert(category);
        return category;
    }

    @Transactional
    public boolean delete(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }

    @Transactional
    public boolean update(Category category) {
        return categoryMapper.update(category) > 0;
    }

    public Category findById(Long id) {
        return categoryMapper.findById(id);
    }

    public List<Category> findAll() {
        return categoryMapper.findAll();
    }

    public List<Category> findByPage(int page, int pageSize, String sortBy, String sortOrder) {
        if (sortBy != null && !sortBy.isEmpty()) {
            if (!isValidSortField(sortBy)) {
                throw new IllegalArgumentException("Invalid sort field");
            }
            if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
                sortOrder = "asc";
            }
        }

        int offset = (page - 1) * pageSize;
        return categoryMapper.findByPage(offset, pageSize, sortBy, sortOrder);
    }

    private boolean isValidSortField(String field) {
        String[] validFields = {"id", "name", "parent_id", "created", "updated"};
        for (String validField : validFields) {
            if (validField.equals(field)) {
                return true;
            }
        }
        return false;
    }

    public long count() {
        return categoryMapper.count();
    }

    public List<Category> search(String keyword) {
        return categoryMapper.search(keyword);
    }

    public List<Category> findByParentId(Long parentId) {
        return categoryMapper.findByParentId(parentId);
    }
} 
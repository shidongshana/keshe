package com.keshe.service;

import com.keshe.entity.Type;
import com.keshe.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Transactional
    public Type create(Type type) {
        typeMapper.insert(type);
        return type;
    }

    @Transactional
    public boolean delete(Long id) {
        return typeMapper.deleteById(id) > 0;
    }

    @Transactional
    public boolean update(Type type) {
        return typeMapper.update(type) > 0;
    }

    public Type findById(Long id) {
        return typeMapper.findById(id);
    }

    public List<Type> findAll() {
        return typeMapper.findAll();
    }

    public List<Type> findByPage(int page, int pageSize, String sortBy, String sortOrder) {
        if (sortBy != null && !sortBy.isEmpty()) {
            if (!isValidSortField(sortBy)) {
                throw new IllegalArgumentException("Invalid sort field");
            }
            if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
                sortOrder = "asc";
            }
        }

        int offset = (page - 1) * pageSize;
        return typeMapper.findByPage(offset, pageSize, sortBy, sortOrder);
    }

    private boolean isValidSortField(String field) {
        String[] validFields = {"id", "name", "category_id", "created", "updated"};
        for (String validField : validFields) {
            if (validField.equals(field)) {
                return true;
            }
        }
        return false;
    }

    public long count() {
        return typeMapper.count();
    }

    public List<Type> search(String keyword) {
        return typeMapper.search(keyword);
    }

    public List<Type> findByCategoryId(Long categoryId) {
        return typeMapper.findByCategoryId(categoryId);
    }
} 
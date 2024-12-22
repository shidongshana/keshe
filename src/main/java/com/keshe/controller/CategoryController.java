package com.keshe.controller;

import com.keshe.entity.Category;
import com.keshe.entity.RestBean;
import com.keshe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 创建分类
    @PostMapping
    public RestBean<Category> create(@RequestBody Category category) {
        Category created = categoryService.create(category);
        return RestBean.success(created);
    }

    // 删除分类
    @DeleteMapping("/{id}")
    public RestBean<Void> delete(@PathVariable Long id) {
        if (categoryService.delete(id)) {
            return RestBean.success(null);
        }
        return RestBean.failure(404, "分类不存在");
    }

    // 更新分类
    @PutMapping("/{id}")
    public RestBean<Void> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        if (categoryService.update(category)) {
            return RestBean.success(null);
        }
        return RestBean.failure(404, "分类不存在");
    }

    // 获取单个分类
    @GetMapping("/{id}")
    public RestBean<Category> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category != null) {
            return RestBean.success(category);
        }
        return RestBean.failure(404, "分类不存在");
    }

    // 获取所有分类
    @GetMapping
    public RestBean<List<Category>> findAll() {
        return RestBean.success(categoryService.findAll());
    }

    // 分页查询
    @GetMapping("/page")
    public RestBean<List<Category>> findByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        try {
            return RestBean.success(categoryService.findByPage(page, pageSize, sortBy, sortOrder));
        } catch (IllegalArgumentException e) {
            return RestBean.failure(400, "Invalid sort parameters");
        } catch (Exception e) {
            return RestBean.failure(500, "Server error");
        }
    }

    // 获取总数
    @GetMapping("/total")
    public RestBean<Long> getTotal() {
        long total = categoryService.count();
        return RestBean.success(total);
    }

    // 搜索分类
    @GetMapping("/search")
    public RestBean<List<Category>> search(@RequestParam String keyword) {
        return RestBean.success(categoryService.search(keyword));
    }

    // 获取子分类
    @GetMapping("/children/{parentId}")
    public RestBean<List<Category>> findByParentId(@PathVariable Long parentId) {
        return RestBean.success(categoryService.findByParentId(parentId));
    }
} 
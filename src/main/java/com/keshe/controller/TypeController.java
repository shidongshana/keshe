package com.keshe.controller;

import com.keshe.entity.Type;
import com.keshe.entity.RestBean;
import com.keshe.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @PostMapping
    public RestBean<Type> create(@RequestBody Type type) {
        Type created = typeService.create(type);
        return RestBean.success(created);
    }

    @DeleteMapping("/{id}")
    public RestBean<Void> delete(@PathVariable Long id) {
        if (typeService.delete(id)) {
            return RestBean.success(null);
        }
        return RestBean.failure(404, "类型不存在");
    }

    @PutMapping("/{id}")
    public RestBean<Void> update(@PathVariable Long id, @RequestBody Type type) {
        type.setId(id);
        if (typeService.update(type)) {
            return RestBean.success(null);
        }
        return RestBean.failure(404, "类型不存在");
    }

    @GetMapping("/{id}")
    public RestBean<Type> findById(@PathVariable Long id) {
        Type type = typeService.findById(id);
        if (type != null) {
            return RestBean.success(type);
        }
        return RestBean.failure(404, "类型不存在");
    }

    @GetMapping
    public RestBean<List<Type>> findAll() {
        return RestBean.success(typeService.findAll());
    }

    @GetMapping("/page")
    public RestBean<List<Type>> findByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        try {
            return RestBean.success(typeService.findByPage(page, pageSize, sortBy, sortOrder));
        } catch (IllegalArgumentException e) {
            return RestBean.failure(400, "Invalid sort parameters");
        } catch (Exception e) {
            return RestBean.failure(500, "Server error");
        }
    }

    @GetMapping("/total")
    public RestBean<Long> getTotal() {
        return RestBean.success(typeService.count());
    }

    @GetMapping("/search")
    public RestBean<List<Type>> search(@RequestParam String keyword) {
        return RestBean.success(typeService.search(keyword));
    }

    @GetMapping("/category/{categoryId}")
    public RestBean<List<Type>> findByCategoryId(@PathVariable Long categoryId) {
        return RestBean.success(typeService.findByCategoryId(categoryId));
    }
} 
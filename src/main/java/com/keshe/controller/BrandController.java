package com.keshe.controller;

import com.keshe.entity.Brand;
import com.keshe.entity.RestBean;
import com.keshe.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand")
public class BrandController {
    
    @Autowired
    private BrandService brandService;

    @GetMapping("/list")
    public RestBean list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return RestBean.success(brandService.findPage(page, limit));
    }

    @GetMapping
    public RestBean findAll() {
        return RestBean.success(brandService.findAll());
    }

    @GetMapping("/detail")
    public RestBean detail(@RequestParam Long id) {
        return RestBean.success(brandService.findById(id));
    }

    @PostMapping("/add")
    public RestBean add(@RequestBody Brand brand) {
        brandService.insert(brand);
        return RestBean.success("添加成功");
    }

    @PostMapping("/update")
    public RestBean update(@RequestBody Brand brand) {
        Brand oldBrand = brandService.findById(brand.getId());
        if (oldBrand == null) {
            return RestBean.failure(500, "品牌不存在");
        }
        if (brand.getName() != null) oldBrand.setName(brand.getName());
        if (brand.getLogo() != null) oldBrand.setLogo(brand.getLogo());
        if (brand.getDescription() != null) oldBrand.setDescription(brand.getDescription());
        if (brand.getStatus() != null) oldBrand.setStatus(brand.getStatus());
        brandService.update(oldBrand);
        return RestBean.success("修改成功");
    }

    @PostMapping("/delete")
    public RestBean delete(@RequestParam Long id) {
        brandService.delete(id);
        return RestBean.success("删除成功");
    }

    @GetMapping("/search")
    public RestBean search(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return RestBean.success(brandService.findByNamePage(name, page, limit));
    }
} 
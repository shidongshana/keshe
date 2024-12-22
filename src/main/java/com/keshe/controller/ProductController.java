package com.keshe.controller;

import com.keshe.entity.Product;
import com.keshe.entity.RestBean;
import com.keshe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;


    @GetMapping("/search")
    public RestBean search(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return RestBean.success(productService.findByNamePage(name, page, limit));
    }

    @GetMapping("/list")
    public RestBean list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return RestBean.success(productService.findPage(page, limit));
    }

    @GetMapping("/detail")
    public RestBean detail(@RequestParam Long id) {
        return RestBean.success(productService.findById(id));
    }

    @PostMapping("/add")
    public RestBean add(@RequestBody Product product) {
        productService.insert(product);
        return RestBean.success("添加成功");
    }

@PostMapping("/update")
public RestBean update(@RequestBody Product product) {
    Product oldProduct = productService.findById(product.getId());
    if (oldProduct == null) {
        return RestBean.failure(500,"商品不存在");
    }
    if (product.getName() != null) oldProduct.setName(product.getName());
    if (product.getCategory_id() != null) oldProduct.setCategory_id(product.getCategory_id());
    if (product.getType_id() != null) oldProduct.setType_id(product.getType_id());
    if (product.getBrand_id() != null) oldProduct.setBrand_id(product.getBrand_id());
    if (product.getPrice() != null) oldProduct.setPrice(product.getPrice());
    if (product.getStock() != null) oldProduct.setStock(product.getStock());
    if (product.getDescription() != null) oldProduct.setDescription(product.getDescription());
    if (product.getStatus() != null) oldProduct.setStatus(product.getStatus());
    productService.update(oldProduct);
    return RestBean.success("修改成功");
}

   @PostMapping("/delete")
    public RestBean delete(@RequestParam Long id) {
         productService.delete(id);
         return RestBean.success("删除成功");
    }

    @GetMapping("/images")
    public RestBean images(@RequestParam Long product_id) {
        return RestBean.success(productService.findImages(product_id));
    }






}

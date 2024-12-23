package com.keshe.controller;

import com.keshe.entity.Product;
import com.keshe.entity.ProductImage;
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
    public RestBean add(@RequestParam Map<String, Object> map) {
        Product product = new Product();
        product.setName((String) map.get("name"));
        product.setCategory_id(Long.parseLong((String) map.get("category_id")));
        product.setType_id(Long.parseLong((String) map.get("type_id")));
        product.setBrand_id(Long.parseLong((String) map.get("brand_id")));
        product.setPrice(Double.parseDouble((String) map.get("price")));
        product.setStock(Integer.parseInt((String) map.get("stock")));
        product.setDescription((String) map.get("description"));
        product.setStatus(Integer.parseInt((String) map.get("status")));
        Long  maxId = productService.findMaxId()+1;
        productService.insert(product);
        ProductImage productImage = new ProductImage();
        productImage.setProduct_id(maxId);
        productImage.setImage_url((String) map.get("image_url"));
        productImage.setIs_main(1L);
        productService.insertImage(productImage);
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

    @PostMapping("/updateImage")
    public RestBean updateImage(@RequestParam Long id ,@RequestParam String image_url) {
        ProductImage productImage = new ProductImage();
        productImage.setId(id);
        productImage.setImage_url(image_url);
        productService.updateImage(productImage);
        return RestBean.success("修改成功");
    }

    @PostMapping("/addImage")
    public RestBean addImage(@RequestParam Long product_id, @RequestParam String image_url, @RequestParam Long is_main) {
        ProductImage productImage = new ProductImage();
        productImage.setProduct_id(product_id);
        productImage.setImage_url(image_url);
        productImage.setIs_main(is_main);
        productService.insertImage(productImage);
        return RestBean.success("添加成功");
    }








}

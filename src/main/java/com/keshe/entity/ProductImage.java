package com.keshe.entity;

import lombok.Data;

@Data
public class ProductImage {
    private Long id;
    private Long product_id;
    private String image_url;
    private Boolean is_main;
}
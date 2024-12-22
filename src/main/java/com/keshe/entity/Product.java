package com.keshe.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private Long category_id;
    private Long type_id;
    private Long brand_id;
    private Double price;
    private Integer stock;
    private String description;
    private Integer status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String main_image;
}
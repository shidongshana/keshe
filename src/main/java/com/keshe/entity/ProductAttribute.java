package com.keshe.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductAttribute {
    private Long id;
    private String name;
    private String description;
    private Integer type;
    private Integer status;
    private LocalDateTime created;
    private LocalDateTime updated;
}
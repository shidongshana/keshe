package com.keshe.entity;

import lombok.Data;

@Data
public class ProductAttributeValue {
    private Long id;
    private Long product_id;
    private Long attribute_id;
    private String value;
}
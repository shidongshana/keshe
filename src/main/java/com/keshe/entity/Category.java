package com.keshe.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {
    private Long id;
    private String name;
    private Long parent_id;
    private String description;
    private Integer status;
    private LocalDateTime created;
    private LocalDateTime updated;
}
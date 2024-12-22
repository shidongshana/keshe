package com.keshe.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Brand {
    private Long id;
    private String name;
    private String logo;
    private String description;
    private Integer status;
    private LocalDateTime created;
    private LocalDateTime updated;
}
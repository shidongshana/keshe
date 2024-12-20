package com.keshe.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysMenu {
    private Long id;
    private Long parentId;
    private String name;
    private String path;
    private String perms;
    private String component;
    private Integer type;
    private String icon;
    private Integer orderNum;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer status;
} 
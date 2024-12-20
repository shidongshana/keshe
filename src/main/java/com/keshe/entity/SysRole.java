package com.keshe.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysRole {
    private Long id;
    private String name;
    private String code;
    private String remark;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer status;
} 
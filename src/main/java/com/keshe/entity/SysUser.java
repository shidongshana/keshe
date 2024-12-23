package com.keshe.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysUser {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String city;
    private Integer status;
    private String avatar;
    private LocalDateTime created;
    private LocalDateTime updated;
    private LocalDateTime lastLogin;
} 
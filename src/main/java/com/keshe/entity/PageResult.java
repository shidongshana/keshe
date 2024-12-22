package com.keshe.entity;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> items;
    private long total;

    public PageResult(List<T> items, long total) {
        this.items = items;
        this.total = total;
    }
} 
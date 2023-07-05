package com.qpf.basic.dto;

import lombok.Data;

/**
 * 分页查询参数基类
 */
@Data
public class BasePageDto {
    //当前页(默认第1页)
    private Integer currentPage = 1;
    //每页显示条数(默认每页5个)
    private Integer pageSize = 5;
    //关键字查询
    private String keyword;
}
package com.qpf.basic.dto;

import lombok.Data;

/**
 * 存放经纬度
 */
@Data
public class PointDto {
    //经度
    private Double lng;
    //维度
    private Double lat;
}
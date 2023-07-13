package com.qpf.echarts.vo;

import lombok.Data;

@Data
public class ECharsShopVo {
    //店铺状态(数字描述)
    private Integer state;
    //店铺状态(字符串描述)
    private String stateName;
    //店铺数量
    private Integer countNum;
}
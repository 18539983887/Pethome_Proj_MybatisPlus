package com.qpf.basic.dto;

import lombok.Data;

/**
 * 手机验证码DTO
 */
@Data
public class SmsCodeDto {
    //手机号码
    private String phone;
    //图形验证码
    private String imageCode;
    //图形验证码的key
    private String imageCodeKey;
}
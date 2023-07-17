package com.qpf.org.dto;

import com.qpf.basic.dto.BasePageDto;
import lombok.Data;

/**
 * 查询类：
 */
@Data
public class WxuserDto{
    private String phone;
    private String verifyCode;
    private String accessToken;
    private String openid;

}
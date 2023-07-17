package com.qpf.org.dto;

import com.qpf.basic.dto.BasePageDto;
import lombok.Data;

@Data
public class WxBinderDto{
    //手机号
    private String phone;
    //手机验证码
    private String verifyCode;
    //权限令牌
    private String accessToken;
    //微信唯一标识
    private String openid;
}

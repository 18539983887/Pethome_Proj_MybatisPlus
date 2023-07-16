package com.qpf.user.dto;

import com.qpf.basic.dto.BasePageDto;
import lombok.Data;

/**
 * 查询类：
 */
@Data
public class UserDto extends BasePageDto{
    //手机验证码
    private String phoneCode;
    //注册手机号
    private String phone;
    //邮箱注册时图片验证码
    private String emailCode;
    //注册邮箱
    private String email;
    //判断 + 注册
    private String password;
    //判断
    private String passwordRepeat;  //确认密码
}
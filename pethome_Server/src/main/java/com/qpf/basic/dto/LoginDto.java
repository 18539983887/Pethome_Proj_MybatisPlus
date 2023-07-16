package com.qpf.basic.dto;
import lombok.Data;

@Data
public class LoginDto {
   //账号
   private String username;
   //密码
   private String password;
   //类型：1表示用户，0表示管理员
   private Integer type;
}
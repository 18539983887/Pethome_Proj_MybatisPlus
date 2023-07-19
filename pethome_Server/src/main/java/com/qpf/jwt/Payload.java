package com.qpf.jwt;
import lombok.Data;

import java.util.Date;

@Data
public class Payload<T> {

    private String id;  // jwt的id(token) - 参考JwtUtils
    private T kernelData;  // 核心数据
    private Date expiration;  // 过期时间 - 参考JwtUtils
}
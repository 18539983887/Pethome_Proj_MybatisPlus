package com.qpf.user.pojo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 实体类：
 */
@Data
@Accessors(chain = true)
public class Logininfo implements Serializable {

    private Long id;
    private String username;
    private String phone;
    private String email;
    private String salt;
    private String password;
    /**
     * 类型 - 0管理员，1用户
     */
    private Integer type;
    /**
     * 启用状态：true可用，false禁用
     */
    private Boolean disable;


}

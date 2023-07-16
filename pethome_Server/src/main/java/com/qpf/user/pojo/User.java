package com.qpf.user.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 实体类：
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {

    private Long id;
    private String username;
    private String phone;
    private String email;
    /**
     * 盐值
     */
    private String salt;
    /**
     * 密码，md5加密加盐
     */
    private String password;
    /**
     * 员工状态 - 1启用，0禁用
     */
    private Integer state;
    private Integer age;
    private LocalDateTime createTime;
    private String headImg;
    private Long loginInfoId;


}

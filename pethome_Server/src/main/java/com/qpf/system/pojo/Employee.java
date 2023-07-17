package com.qpf.system.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 实体类：
 */
@Data
@Accessors(chain = true)
public class Employee implements Serializable {

    /**
     * 员工ID
     */
    private Long id;
    /**
     * 员工名称
     */
    private String username;
    /**
     * 员工电话
     */
    private String phone;
    /**
     * 员工邮箱
     */
    private String email;
    /**
     * 盐值
     */
    private String salt;
    /**
     * 密码
     */
    private String password;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 员工状态 - 1启用，0禁用
     */
    private Integer state;
    /**
     * 所属部门ID
     */
    private Long departmentId;
    /**
     * 登录信息ID
     */
    private Long loginInfoId;
    /**
     * 店铺ID
     */
    private Long shopId;


}

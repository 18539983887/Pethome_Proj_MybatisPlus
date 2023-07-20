package com.qpf.org.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 实体类：员工
 */
@Data
@TableName("t_employee")
public class Employee {
    //员工ID
    @TableId
    private Long id;
    //员工名称
    private String username;
    //员工电话
    private String phone;
    //员工邮箱
    private String email;
    //员工盐值
    private String salt;
    //员工密码
    private String password;
    //员工年龄
    private Integer age;
    //员工状态：1启用 0禁用
    private Integer state = 1;
    //员工所属部门ID
    private Long departmentId;
    //店铺ID：为空=平台系统管理员，不为空=店铺管理员
    private Long shopId;
    //所属部门
    @TableField(exist = false)
    private Department dept;
    //登录信息id
    private Long logininfoId;
    //所属角色们(员工可以有多个角色)
    @TableField(exist = false)
    private List<Long> roleIds;
}
package com.qpf.org.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体类：部门
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_department")
public class Department {
    //部门ID
    @TableId
    private Long id;
    //部门编号
    private String sn;
    //部门名称
    private String name;
    //部门路径
    private String dirPath;
    //部门状态：1启用,0禁用
    private Integer state = 1;
    //部门经理ID
    private Long managerId;
    //上级部门ID
    private Long parentId;
    //
    @TableField(exist = false)
    private Employee employee;
    @TableField(exist = false)
    private Department parent;
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Department> children=new ArrayList<>();
}
package com.qpf.org.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.time.LocalDateTime;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

/**
 * 实体类：
 */
@Data
@Accessors(chain = true)
public class Shop implements Serializable {

    private Long id;
    /**
     * 店铺名称
     */
    @Excel(name = "店铺名称", orderNum = "1", width = 30)
    private String name;
    /**
     * 店铺座机
     */
    @Excel(name = "店铺电话", orderNum = "2", width = 30)
    private String tel;
    /**
     * 入驻时间
     */
    @Excel(name = "入驻时间", orderNum = "3", width = 30, exportFormat = "yyyy-MM-dd")
    private Date registerTime;
    /**
     * 店铺状态：1待审核，2审核通过待激活，3激活成功，4驳回
     */
    @Excel(name = "店铺状态", orderNum = "4", width = 30, replace = {"待审核_1", "待激活_2", "激活成功_3", "驳回_4"}, addressList = true)
    private Integer state;

    /**
     * 店铺地址
     */
    @Excel(name = "店铺地址", orderNum = "5", width = 30)
    private String address;
    /**
     * 店铺logo
     */
    @Excel(name = "店铺logo", orderNum = "6", width = 30)
    private String logo;
    /**
     * 店铺管理员ID
     */
    private Long adminId;

    @TableField(exist = false)
    private Employee admin;

}

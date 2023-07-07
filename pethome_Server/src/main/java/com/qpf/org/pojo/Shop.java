package com.qpf.org.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class Shop implements Serializable {

    private Long id;
    /**
     * 店铺名称
     */
    private String name;
    /**
     * 店铺座机
     */
    private String tel;
    /**
     * 入驻时间
     */
    private LocalDate registerTime;
    /**
     * 店铺状态：1待审核，2审核通过待激活，3激活成功，4驳回
     */
    private Integer state;
    /**
     * 店铺地址
     */
    private String address;
    /**
     * 店铺logo
     */
    private String logo;
    /**
     * 店铺管理员ID
     */
    private Long adminId;


}

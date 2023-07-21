package com.qpf.pet.pojo;

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
public class SearchMasterMsg implements Serializable {

    private Long id;
    private String title;
    /**
     * 0 待审核 1 审核通过 -1 驳回
     */
    private Integer state;
    /**
     * 宠物名称
     */
    private String name;
    private Integer age;
    private Boolean gender;
    /**
     * 毛色
     */
    private String coatColor;
    /**
     * fastdfs地址1,fastdfs地址1
     */
    private String resources;
    /**
     * 类型
     */
    private Long petType;
    private BigDecimal price;
    private String address;
    private Long userId;
    /**
     * 店铺id 消息分配的店铺
     */
    private Long shopId;
    private String note;


}
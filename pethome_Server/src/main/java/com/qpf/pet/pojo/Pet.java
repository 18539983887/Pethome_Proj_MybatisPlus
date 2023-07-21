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
public class Pet implements Serializable {

    private Long id;
    private String name;
    private BigDecimal costprice;
    private BigDecimal saleprice;
    /**
     * 类型id
     */
    private Long typeId;
    private String resources;
    /**
     * 状态：0下架 1上架
     */
    private Integer state;
    private LocalDateTime offsaletime;
    private LocalDateTime onsaletime;
    private LocalDateTime createtime;
    /**
     * 店铺Id 如果被领养店铺id为null
     */
    private Long shopId;
    /**
     * 如果被领养为领养用户id
     */
    private Long userId;
    private Long searchMasterMsgId;


}

package com.qpf.org.pojo;

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
public class ShopAuditLog implements Serializable {

    private Long id;
    private Integer state;
    private Long shopId;
    private Long auditId;
    private LocalDateTime auditTime;
    private String note;


}

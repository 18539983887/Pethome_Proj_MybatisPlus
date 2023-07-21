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
public class SearchMasterMsgAuditLog implements Serializable {

    private Long id;
    /**
     * 消息id
     */
    private Long msgId;
    /**
     * 状态 0失败 1成功
     */
    private Integer state;
    /**
     * 审核人 如果为null系统审核
     */
    private Long auditId;
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    /**
     * 备注
     */
    private String note;


}

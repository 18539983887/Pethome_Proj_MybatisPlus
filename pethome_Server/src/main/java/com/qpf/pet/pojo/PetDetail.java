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
public class PetDetail implements Serializable {

    private Long id;
    private Long petId;
    /**
     * 领养须知
     */
    private String adoptNotice;
    /**
     * 简介
     */
    private String intro;


}

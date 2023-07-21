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
public class PetType implements Serializable {

    private Long id;
    private String name;
    private String description;
    /**
     * 父类型id
     */
    private Long parentId;


}
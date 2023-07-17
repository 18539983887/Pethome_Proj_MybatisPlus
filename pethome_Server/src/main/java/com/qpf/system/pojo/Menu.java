package com.qpf.system.pojo;

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
public class Menu implements Serializable {

    private Long id;
    private String name;
    private String component;
    private String url;
    private String icon;
    private Integer index;
    private Long parentId;
    private String intro;
    private Boolean state;


}

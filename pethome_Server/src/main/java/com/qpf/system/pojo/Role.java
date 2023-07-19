package com.qpf.system.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 实体类：
 */
@Data
@Accessors(chain = true)
public class Role implements Serializable {

    private Long id;
    private String name;
    private String sn;
    @TableField(exist = false)
    private List<Long> permissionIds = new ArrayList<>();
    @TableField(exist = false)
    private List<Long> menuIds = new ArrayList<>();


}

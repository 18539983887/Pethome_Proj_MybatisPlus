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
public class RolePermission implements Serializable {

    private Long roleId;
    private Long permissionId;
    private Long id;


}

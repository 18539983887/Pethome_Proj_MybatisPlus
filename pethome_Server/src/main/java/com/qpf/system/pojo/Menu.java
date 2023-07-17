package com.qpf.system.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    @TableField("`index`")
    private Integer index;
    private Long parentId;
    private String intro;
    private Boolean state;
    //父级菜单
    @TableField(exist = false)
    private Menu parent;
    //当前菜单下的子菜单
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children =new ArrayList<>();


}

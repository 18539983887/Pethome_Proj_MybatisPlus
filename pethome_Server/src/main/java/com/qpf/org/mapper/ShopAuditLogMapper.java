package com.qpf.org.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qpf.org.pojo.ShopAuditLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 后端Service接口；
*/
@Mapper
@Repository
public interface ShopAuditLogMapper extends BaseMapper<ShopAuditLog> {

    /**
     * 分页：查询当前页数据
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return
     */
    List<ShopAuditLog> findByPage(@Param("page") IPage<ShopAuditLog> page,@Param("ew") QueryWrapper<ShopAuditLog> queryWrapper);
}

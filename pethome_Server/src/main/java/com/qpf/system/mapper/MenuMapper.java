package com.qpf.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qpf.system.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 后端Service接口；
*/
@Mapper
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 分页：查询当前页数据
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return
     */
    List<Menu> findByPage(@Param("page") IPage<Menu> page,@Param("ew") QueryWrapper<Menu> queryWrapper);

    List<Menu> getMenusByLogininfoId(Long id);
}

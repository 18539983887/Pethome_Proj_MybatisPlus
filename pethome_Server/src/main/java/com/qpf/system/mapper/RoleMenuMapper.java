package com.qpf.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qpf.system.pojo.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 后端Service接口；
*/
@Mapper
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 分页：查询当前页数据
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return
     */
    List<RoleMenu> findByPage(@Param("page") IPage<RoleMenu> page,@Param("ew") QueryWrapper<RoleMenu> queryWrapper);
    /**
     * 根据角色ID，查询角色所拥有的菜单
     * @param roleId 角色id
     * @return
     */
    List<Long> findMenuIdByRoleId(Long roleId);
}

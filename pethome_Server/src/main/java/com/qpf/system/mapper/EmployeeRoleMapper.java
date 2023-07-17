package com.qpf.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qpf.system.pojo.EmployeeRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 后端Service接口；
*/
@Mapper
@Repository
public interface EmployeeRoleMapper extends BaseMapper<EmployeeRole> {

    /**
     * 分页：查询当前页数据
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return
     */
    List<EmployeeRole> findByPage(@Param("page") IPage<EmployeeRole> page,@Param("ew") QueryWrapper<EmployeeRole> queryWrapper);
    /**
     * 根据员工Id，查询员工所属角色ID
     * @param employeeId 员工id
     * @return
     */
    List<Long> findRoleIdByEmployeeId(Long employeeId);
}

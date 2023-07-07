package com.qpf.org.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qpf.org.dto.EmployeeDto;
import com.qpf.org.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    List<Employee> findByPage(@Param("page") IPage<Employee> page, @Param("ew") QueryWrapper<Employee> queryWrapper);
}

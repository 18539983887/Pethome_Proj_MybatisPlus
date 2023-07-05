package com.qpf.org.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.github.pagehelper.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qpf.basic.vo.AjaxResult;
import com.qpf.org.dto.DepartmentDto;
import com.qpf.org.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    List<Department> findByPage(@Param("page") IPage<Department> page,@Param("ew") QueryWrapper<Department> qw);
//    void add(Department department);
//    void delById(Long id);
//    void patchDel(Long[] ids);
//    void update(Department department);
//    Department findOne(Long id);
//    List<Department> findAll();
//    List<Department> findByPage(DepartmentDto departmentDto);

}

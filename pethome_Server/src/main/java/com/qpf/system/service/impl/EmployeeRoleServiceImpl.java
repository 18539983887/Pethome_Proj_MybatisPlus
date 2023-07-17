package com.qpf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.system.pojo.EmployeeRole;
import com.qpf.system.service.IEmployeeRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.system.mapper.EmployeeRoleMapper;
import com.qpf.system.dto.EmployeeRoleDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class EmployeeRoleServiceImpl implements IEmployeeRoleService {

    @Autowired
    private EmployeeRoleMapper employeeRoleMapper;

    @Override
    public void add(EmployeeRole employeeRole){
        employeeRoleMapper.insert(employeeRole);
    }

    @Override
    public void deleteById(Long id){
        employeeRoleMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        employeeRoleMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(EmployeeRole employeeRole){
        employeeRoleMapper.updateById(employeeRole);
    }

    @Override
    public EmployeeRole findOne(Long id){
        return employeeRoleMapper.selectById(id);
    }

    @Override
    public List<EmployeeRole>findAll(){
        return employeeRoleMapper.selectList(null);
    }

    @Override
    public IPage<EmployeeRole>findByPage(EmployeeRoleDto employeeRoleDto){
        //1.创建查询条件
        QueryWrapper<EmployeeRole> qw = new QueryWrapper<>();
        //qw.like("xxx",employeeRoleDto.getXxx());
        //qw.or();
        //qw.like("xxx",employeeRoleDto.getXxx());
        //2.组件分页数据
        IPage<EmployeeRole> page = new Page<>(employeeRoleDto.getCurrentPage(), employeeRoleDto.getPageSize());
        page.setRecords(employeeRoleMapper.findByPage(page,qw));
        page.setTotal(employeeRoleMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

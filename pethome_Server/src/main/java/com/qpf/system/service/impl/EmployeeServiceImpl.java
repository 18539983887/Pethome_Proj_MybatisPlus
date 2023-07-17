package com.qpf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.system.pojo.Employee;
import com.qpf.system.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.system.mapper.EmployeeMapper;
import com.qpf.system.dto.EmployeeDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public void add(Employee employee){
        employeeMapper.insert(employee);
    }

    @Override
    public void deleteById(Long id){
        employeeMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        employeeMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Employee employee){
        employeeMapper.updateById(employee);
    }

    @Override
    public Employee findOne(Long id){
        return employeeMapper.selectById(id);
    }

    @Override
    public List<Employee>findAll(){
        return employeeMapper.selectList(null);
    }

    @Override
    public IPage<Employee>findByPage(EmployeeDto employeeDto){
        //1.创建查询条件
        QueryWrapper<Employee> qw = new QueryWrapper<>();
        //qw.like("xxx",employeeDto.getXxx());
        //qw.or();
        //qw.like("xxx",employeeDto.getXxx());
        //2.组件分页数据
        IPage<Employee> page = new Page<>(employeeDto.getCurrentPage(), employeeDto.getPageSize());
        page.setRecords(employeeMapper.findByPage(page,qw));
        page.setTotal(employeeMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

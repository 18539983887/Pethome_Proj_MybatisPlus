package com.qpf.org.service.impl;

//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.org.dto.EmployeeDto;
import com.qpf.org.mapper.EmployeeMapper;
import com.qpf.org.pojo.Employee;
import com.qpf.org.service.IEmployeeService;
import com.qpf.user.mapper.LoginInfoMapper;
import com.qpf.user.pojo.Logininfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private LoginInfoMapper loginInfoMapper;

    @Override
    public void add(Employee employee) {
        //处理盐值和密码
        String salt = RandomUtil.randomString(32);
        String md5Pwd = SecureUtil.md5(employee.getPassword() + salt);
        employee.setSalt(salt);
        employee.setPassword(md5Pwd);

        //先添加logininfo
        Logininfo logininfo = new Logininfo();
        BeanUtil.copyProperties(employee, logininfo);
        logininfo.setType(0);       //管理员
        logininfo.setDisable(true); //启动
        loginInfoMapper.insert(logininfo);//获取到自增ID

        //添加employee的数据
        employee.setLoginInfoId(logininfo.getId());
        employeeMapper.insert(employee);
    }

    @Override
    public void del(Long id) {
        employeeMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(Long[] ids) {
        employeeMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void updateState(Long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee.getState() == 0) {
            employee.setState(1);
        } else {
            employee.setState(0);
        }
        employeeMapper.updateById(employee);
    }

    @Override
    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    @Override
    public Employee findOne(Long id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public List<Employee> findAll() {
        return employeeMapper.selectList(null);
    }


    @Override
    public IPage<Employee> findByPage(EmployeeDto employeeDto) {
        QueryWrapper<Employee> qw = new QueryWrapper<>();
        qw.like("username", employeeDto.getKeyword());
        qw.or();
        qw.like("phone", employeeDto.getKeyword());
        IPage<Employee> page = new Page<>(employeeDto.getCurrentPage(), employeeDto.getPageSize());
        page.setRecords(employeeMapper.findByPage(page, qw));
        page.setTotal(employeeMapper.selectCount(qw));
        return page;
    }
}

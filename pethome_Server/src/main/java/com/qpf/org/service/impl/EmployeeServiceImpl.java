package com.qpf.org.service.impl;

//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.org.dto.EmployeeDto;
import com.qpf.org.mapper.EmployeeMapper;
import com.qpf.org.pojo.Employee;
import com.qpf.org.service.IEmployeeService;
import com.qpf.system.mapper.EmployeeRoleMapper;
import com.qpf.system.pojo.EmployeeRole;
import com.qpf.user.mapper.LoginInfoMapper;
import com.qpf.user.pojo.Logininfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private LoginInfoMapper loginInfoMapper;
    @Autowired
    private EmployeeRoleMapper employeeRoleMapper;

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
        employee.setLogininfoId(logininfo.getId());
        employeeMapper.insert(employee);
        //4.添加员工所对应的角色
        if (ObjectUtil.isNotEmpty(employee.getRoleIds())) {
            List<Long> roleIds = employee.getRoleIds();
            for (Long roleId : roleIds) {
                employeeRoleMapper.insert(new EmployeeRole().setRoleId(roleId).setEmployeeId(employee.getId()));
            }
        }
    }

    @Transactional
    @Override
    public void del(Long id) {
        //2.删除员工对应的角色信息
        UpdateWrapper<EmployeeRole> uw = new UpdateWrapper<>();
        uw.eq("employee_id", id);
        employeeRoleMapper.delete(uw);

        //1.先查询员工信息
        Employee employee = employeeMapper.selectById(id);
        //3.删除employee(关联数据先删除从表)
        employeeMapper.deleteById(id);
        //4.删除logininfo(再删除主表)
        loginInfoMapper.deleteById(employee.getLogininfoId());
    }

    @Transactional
    @Override
    public void patchDel(Long[] ids) {
//        //1.删除员工对应的角色们
//        for (Long id : ids) {
//            UpdateWrapper<EmployeeRole> uw = new UpdateWrapper<>();
//            uw.ge("employee_id", id);
//            employeeRoleMapper.delete(uw);
//        } -----------------效率低---------
//        1.
        employeeRoleMapper.delete(new UpdateWrapper<EmployeeRole>().in("employee_id",ids));
        //2.查询所有员工信息，并获取他们的登录信息
        List<Employee> list = employeeMapper.selectBatchIds(Arrays.asList(ids));
        List<Long> loginInfoIds = list.stream().map(emp -> emp.getLogininfoId()).collect(Collectors.toList());
        //3.删除所有员工
        employeeMapper.deleteBatchIds(Arrays.asList(ids));
        //4.删除员工的登录信息
        loginInfoMapper.deleteBatchIds(loginInfoIds);
    }

    @Override
    public void updateState(Long id) {
        Employee employee=new Employee();
        //1.修改时,重新处理密码(处理盐值和密),当然也可以不处理
        if (employee.getPassword() != null) {
            //处理盐值和密码
            String salt = RandomUtil.randomString(32);
            String md5Pwd = SecureUtil.md5(employee.getPassword() + salt);
            employee.setSalt(salt);
            employee.setPassword(md5Pwd);
        }

        //2.修改登陆信息
        Logininfo logininfo = loginInfoMapper.selectById(employee.getLogininfoId());
        if (employee.getLogininfoId() == null || logininfo == null) {
            logininfo = new Logininfo();
        }
        //2.1 把员工的数据同步到登录信息中,不同步id
        BeanUtil.copyProperties(employee, logininfo, "id");
        //2.2 设置登录信息的状态
        logininfo.setDisable(employee.getState() == 1);
        //2.3 判断,有登录信息就是修改,没登录信息就是添加
        if (logininfo.getId() != null) {
            loginInfoMapper.updateById(logininfo);
        } else {
            //2.4 修改登录信息
            loginInfoMapper.insert(logininfo);
            //2.5 把新添加的登录信息关联到员工表中
            employee.setLogininfoId(logininfo.getId());
        }

        //3.更新员工信息
        employeeMapper.updateById(employee);

        //4.更新员工角色
        if (ObjectUtil.isNotEmpty(employee.getRoleIds())) {
            //4.1 批量删除
            UpdateWrapper<EmployeeRole> uw = new UpdateWrapper<>();
            uw.ge("employee_id", employee.getId());
            employeeRoleMapper.delete(uw);
            //4.2 批量添加
            List<Long> roleIds = employee.getRoleIds();
            for (Long roleId : roleIds) {
                employeeRoleMapper.insert(new EmployeeRole().setRoleId(roleId).setEmployeeId(employee.getId()));
            }
        }
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

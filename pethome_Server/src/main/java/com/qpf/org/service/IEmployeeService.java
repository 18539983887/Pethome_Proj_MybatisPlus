package com.qpf.org.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.github.pagehelper.PageInfo;
import com.qpf.org.dto.EmployeeDto;
import com.qpf.org.pojo.Employee;

import java.util.List;

public interface IEmployeeService {
    /**
     * 添加一个对象
     * @param employee
     */
    void add(Employee employee);

    /**
     * 删除一个对象
     * @param id
     */
    void del(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void patchDel(Long[] ids);

    /**
     * 修改用户状态
     * @param id
     */
    void updateState(Long id);

    /**
     * 更新一个对象
     * @param employee
     */
    void update(Employee employee);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    Employee findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<Employee> findAll();

    /**
     * 分页查询
     * @param employeeDto 分页参数
     * @return
     */
    IPage<Employee> findByPage(EmployeeDto employeeDto);
}

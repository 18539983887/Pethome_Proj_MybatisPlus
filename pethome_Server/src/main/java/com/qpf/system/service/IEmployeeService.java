package com.qpf.system.service;

import com.qpf.system.pojo.Employee;
import com.qpf.system.dto.EmployeeDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
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
    void deleteById(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void patchDel(List<Long> ids);

    /**
     * 更新一个对象
     * @param employee
     */
    void updateById(Employee employee);

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
     * @param  employeeDto 分页参数
     * @return
     */
    IPage<Employee> findByPage(EmployeeDto employeeDto);
}

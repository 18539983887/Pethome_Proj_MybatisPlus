package com.qpf.system.service;

import com.qpf.system.pojo.EmployeeRole;
import com.qpf.system.dto.EmployeeRoleDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IEmployeeRoleService {
    /**
     * 添加一个对象
     * @param employeeRole
     */
    void add(EmployeeRole employeeRole);

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
     * @param employeeRole
     */
    void updateById(EmployeeRole employeeRole);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    EmployeeRole findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<EmployeeRole> findAll();

    /**
     * 分页查询
     * @param  employeeRoleDto 分页参数
     * @return
     */
    IPage<EmployeeRole> findByPage(EmployeeRoleDto employeeRoleDto);
}

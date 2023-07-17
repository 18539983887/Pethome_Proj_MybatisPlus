package com.qpf.system.controller;

import com.qpf.system.service.IEmployeeRoleService;
import com.qpf.system.pojo.EmployeeRole;
import com.qpf.system.dto.EmployeeRoleDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/employeeRole")
public class EmployeeRoleController {

    @Autowired
    public IEmployeeRoleService employeeRoleService;

    /**
     * 接口：添加或修改
     * @param employeeRole 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody EmployeeRole employeeRole) {
        if ( employeeRole.getId() != null){
            employeeRoleService.updateById(employeeRole);
        }else{
            employeeRoleService.add(employeeRole);
        }
        return new AjaxResult();

    }

    /**
    * 接口：删除
    * @param id
    * @return AjaxResult 响应给前端
    */
    @DeleteMapping(value = "/{id}")
    public AjaxResult del(@PathVariable("id") Long id) {
        employeeRoleService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        employeeRoleService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public EmployeeRole findOne(@PathVariable("id") Long id) {
        return employeeRoleService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<EmployeeRole> findAll() {
        return employeeRoleService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param employeeRoleDto 查询对象
     * @return IPage<EmployeeRole> 分页对象
     */
    @PostMapping
    public IPage<EmployeeRole> findByPage(@RequestBody EmployeeRoleDto employeeRoleDto) {
        return employeeRoleService.findByPage(employeeRoleDto);
    }
}

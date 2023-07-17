package com.qpf.system.controller;

import com.qpf.system.service.IEmployeeService;
import com.qpf.system.pojo.Employee;
import com.qpf.system.dto.EmployeeDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    public IEmployeeService employeeService;

    /**
     * 接口：添加或修改
     * @param employee 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Employee employee) {
        if ( employee.getId() != null){
            employeeService.updateById(employee);
        }else{
            employeeService.add(employee);
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
        employeeService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        employeeService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public Employee findOne(@PathVariable("id") Long id) {
        return employeeService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<Employee> findAll() {
        return employeeService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param employeeDto 查询对象
     * @return IPage<Employee> 分页对象
     */
    @PostMapping
    public IPage<Employee> findByPage(@RequestBody EmployeeDto employeeDto) {
        return employeeService.findByPage(employeeDto);
    }
}

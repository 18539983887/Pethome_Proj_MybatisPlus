package com.qpf.org.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qpf.org.dto.EmployeeDto;
import com.qpf.org.pojo.Employee;
import com.qpf.org.service.IEmployeeService;
import com.qpf.basic.vo.AjaxResult;
//import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工操作Controller
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;
    /**
     * 接口：查询单个对象
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Employee findOne(@PathVariable Long id){
        return employeeService.findOne(id);
    }

    /**
     * 接口：查询所有对象
     * @return
     */
    @GetMapping
    public List<Employee> findAll(){
        return employeeService.findAll();
    }

    /**
     * 接口：删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public AjaxResult del(@PathVariable Long id){
        employeeService.del(id);
        return new AjaxResult();
    }


    /**
     * 接口：批量删除
     * @param ids
     * @return
     */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody Long[] ids){
        employeeService.patchDel(ids);
        return new AjaxResult();
    }

    /**
     * 接口：修改用户状态
     * @param id 用户id
     * @return
     */
    @PutMapping("/{id}")
    public AjaxResult updateState(@PathVariable Long id) {
        employeeService.updateState(id);
        return new AjaxResult();
    }

    /**
     * 接口：添加或修改
     * @param employee
     * @return
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody @Validated Employee employee){
        if(employee.getId()==null){//添加
            System.out.println(employee);
            employeeService.add(employee);
        }else{
            System.out.println(employee);
            employeeService.update(employee);
        }
        return new AjaxResult();
    }

    /**
     * 接口：分页+高级查询
     * @param employeeDto
     * @return
     */
    @PostMapping
    public IPage<Employee> findByPage(@RequestBody EmployeeDto employeeDto){
        return employeeService.findByPage(employeeDto);
    }

}
package com.qpf.org.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.github.pagehelper.PageInfo;
import com.qpf.basic.vo.AjaxResult;
import com.qpf.org.dto.DepartmentDto;
import com.qpf.org.pojo.Department;
import com.qpf.org.pojo.Employee;
import com.qpf.org.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private IDepartmentService idepartmentService;
//分页查询
    @PostMapping
    public IPage<Department> findByPage(@RequestBody DepartmentDto departmentDto) {

        return idepartmentService.findByPage(departmentDto);
    }
    @GetMapping
    public List<Department> findAll(){
        System.out.println(idepartmentService.findAll());
        return idepartmentService.findAll();

    }
//查询单个
    @GetMapping("/{id}")
    public Department findOne(@PathVariable("id") Long id) {
        return idepartmentService.findOne(id);
    }
//删除单个
    @DeleteMapping("/{id}")
    public AjaxResult delById(@PathVariable("id") Long id) {
         idepartmentService.delById(id);
         return AjaxResult.me();
    }
//批量删除
    @PatchMapping
    public AjaxResult patchDel(Long[] ids) {
        idepartmentService.patchDel(ids);
        return AjaxResult.me();
    }
//增加和修改
    @PutMapping
    public AjaxResult addAndUpdate(@RequestBody Department department) {
        if (department.getId() == null) {
            idepartmentService.add(department);
        } else {
            idepartmentService.update(department);
        }
        return new AjaxResult();
    }
//部门树
    @GetMapping("/deptTree")
    public List<Department> getDeptTree() {
        return idepartmentService.getDeptTree();
    }


}

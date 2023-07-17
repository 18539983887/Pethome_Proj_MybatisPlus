package com.qpf.system.controller;

import com.qpf.system.service.IRolePermissionService;
import com.qpf.system.pojo.RolePermission;
import com.qpf.system.dto.RolePermissionDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/rolePermission")
public class RolePermissionController {

    @Autowired
    public IRolePermissionService rolePermissionService;

    /**
     * 接口：添加或修改
     * @param rolePermission 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody RolePermission rolePermission) {
        if ( rolePermission.getId() != null){
            rolePermissionService.updateById(rolePermission);
        }else{
            rolePermissionService.add(rolePermission);
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
        rolePermissionService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        rolePermissionService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public RolePermission findOne(@PathVariable("id") Long id) {
        return rolePermissionService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<RolePermission> findAll() {
        return rolePermissionService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param rolePermissionDto 查询对象
     * @return IPage<RolePermission> 分页对象
     */
    @PostMapping
    public IPage<RolePermission> findByPage(@RequestBody RolePermissionDto rolePermissionDto) {
        return rolePermissionService.findByPage(rolePermissionDto);
    }
}

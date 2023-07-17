package com.qpf.system.controller;

import com.qpf.system.service.IPermissionService;
import com.qpf.system.pojo.Permission;
import com.qpf.system.dto.PermissionDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    public IPermissionService permissionService;

    /**
     * 接口：添加或修改
     * @param permission 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Permission permission) {
        if ( permission.getId() != null){
            permissionService.updateById(permission);
        }else{
            permissionService.add(permission);
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
        permissionService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        permissionService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public Permission findOne(@PathVariable("id") Long id) {
        return permissionService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<Permission> findAll() {
        return permissionService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param permissionDto 查询对象
     * @return IPage<Permission> 分页对象
     */
    @PostMapping
    public IPage<Permission> findByPage(@RequestBody PermissionDto permissionDto) {
        return permissionService.findByPage(permissionDto);
    }
}

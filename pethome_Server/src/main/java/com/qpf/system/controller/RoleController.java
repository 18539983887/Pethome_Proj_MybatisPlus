package com.qpf.system.controller;

import com.qpf.system.service.IRoleService;
import com.qpf.system.pojo.Role;
import com.qpf.system.dto.RoleDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    public IRoleService roleService;

    /**
     * 接口：添加或修改
     * @param role 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Role role) {
        if ( role.getId() != null){
            roleService.updateById(role);
        }else{
            roleService.add(role);
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
        roleService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        roleService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public Role findOne(@PathVariable("id") Long id) {
        return roleService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<Role> findAll() {
        return roleService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param roleDto 查询对象
     * @return IPage<Role> 分页对象
     */
    @PostMapping
    public IPage<Role> findByPage(@RequestBody RoleDto roleDto) {
        return roleService.findByPage(roleDto);
    }
}

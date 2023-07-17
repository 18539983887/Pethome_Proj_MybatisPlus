package com.qpf.system.controller;

import com.qpf.system.service.IRoleMenuService;
import com.qpf.system.pojo.RoleMenu;
import com.qpf.system.dto.RoleMenuDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuController {

    @Autowired
    public IRoleMenuService roleMenuService;

    /**
     * 接口：添加或修改
     * @param roleMenu 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody RoleMenu roleMenu) {
        if ( roleMenu.getId() != null){
            roleMenuService.updateById(roleMenu);
        }else{
            roleMenuService.add(roleMenu);
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
        roleMenuService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        roleMenuService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public RoleMenu findOne(@PathVariable("id") Long id) {
        return roleMenuService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<RoleMenu> findAll() {
        return roleMenuService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param roleMenuDto 查询对象
     * @return IPage<RoleMenu> 分页对象
     */
    @PostMapping
    public IPage<RoleMenu> findByPage(@RequestBody RoleMenuDto roleMenuDto) {
        return roleMenuService.findByPage(roleMenuDto);
    }
}

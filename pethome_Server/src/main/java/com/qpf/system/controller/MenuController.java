package com.qpf.system.controller;

import com.qpf.system.service.IMenuService;
import com.qpf.system.pojo.Menu;
import com.qpf.system.dto.MenuDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    public IMenuService menuService;

    /**
     * 接口：添加或修改
     * @param menu 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Menu menu) {
        if ( menu.getId() != null){
            menuService.updateById(menu);
        }else{
            menuService.add(menu);
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
        menuService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        menuService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public Menu findOne(@PathVariable("id") Long id) {
        return menuService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<Menu> findAll() {
        return menuService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param menuDto 查询对象
     * @return IPage<Menu> 分页对象
     */
    @PostMapping
    public IPage<Menu> findByPage(@RequestBody MenuDto menuDto) {
        return menuService.findByPage(menuDto);
    }
    @GetMapping("/menuTree")
    public List<Menu> menuTree(){
        return menuService.menuTree();
    }
    @GetMapping("/menuPath")
    public Map<Long,String> menuPath(){
        return menuService.menuPath();
    }
}

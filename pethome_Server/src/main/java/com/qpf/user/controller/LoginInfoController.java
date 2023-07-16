package com.qpf.user.controller;

import com.qpf.user.service.ILogininfoService;
import com.qpf.user.pojo.Logininfo;
import com.qpf.user.dto.LoginInfoDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/logininfo")
public class LoginInfoController {

    @Autowired
    public ILogininfoService logininfoService;

    /**
     * 接口：添加或修改
     * @param logininfo 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Logininfo logininfo) {
        if ( logininfo.getId() != null){
            logininfoService.updateById(logininfo);
        }else{
            logininfoService.add(logininfo);
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
        logininfoService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        logininfoService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public Logininfo findOne(@PathVariable("id") Long id) {
        return logininfoService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<Logininfo> findAll() {
        return logininfoService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param logininfoDto 查询对象
     * @return IPage<Logininfo> 分页对象
     */
    @PostMapping
    public IPage<Logininfo> findByPage(@RequestBody LoginInfoDto logininfoDto) {
        return logininfoService.findByPage(logininfoDto);
    }
}

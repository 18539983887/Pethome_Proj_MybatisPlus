package com.qpf.org.controller;

import com.qpf.org.service.IWxuserService;
import com.qpf.org.pojo.Wxuser;
import com.qpf.org.dto.WxuserDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/wxuser")
public class WxuserController {

    @Autowired
    public IWxuserService wxuserService;

    /**
     * 接口：添加或修改
     * @param wxuser 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Wxuser wxuser) {
        if ( wxuser.getId() != null){
            wxuserService.updateById(wxuser);
        }else{
            wxuserService.add(wxuser);
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
        wxuserService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        wxuserService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public Wxuser findOne(@PathVariable("id") Long id) {
        return wxuserService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<Wxuser> findAll() {
        return wxuserService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param wxuserDto 查询对象
     * @return IPage<Wxuser> 分页对象
     */
    @PostMapping
    public IPage<Wxuser> findByPage(@RequestBody WxuserDto wxuserDto) {
        return wxuserService.findByPage(wxuserDto);
    }
}

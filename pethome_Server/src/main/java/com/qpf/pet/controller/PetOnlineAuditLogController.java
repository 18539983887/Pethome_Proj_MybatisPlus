package com.qpf.pet.controller;

import com.qpf.pet.service.IPetOnlineAuditLogService;
import com.qpf.pet.pojo.PetOnlineAuditLog;
import com.qpf.pet.dto.PetOnlineAuditLogDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/petOnlineAuditLog")
public class PetOnlineAuditLogController {

    @Autowired
    public IPetOnlineAuditLogService petOnlineAuditLogService;

    /**
     * 接口：添加或修改
     * @param petOnlineAuditLog 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody PetOnlineAuditLog petOnlineAuditLog) {
        if ( petOnlineAuditLog.getId() != null){
            petOnlineAuditLogService.updateById(petOnlineAuditLog);
        }else{
            petOnlineAuditLogService.add(petOnlineAuditLog);
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
        petOnlineAuditLogService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        petOnlineAuditLogService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public PetOnlineAuditLog findOne(@PathVariable("id") Long id) {
        return petOnlineAuditLogService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<PetOnlineAuditLog> findAll() {
        return petOnlineAuditLogService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param petOnlineAuditLogDto 查询对象
     * @return IPage<PetOnlineAuditLog> 分页对象
     */
    @PostMapping
    public IPage<PetOnlineAuditLog> findByPage(@RequestBody PetOnlineAuditLogDto petOnlineAuditLogDto) {
        return petOnlineAuditLogService.findByPage(petOnlineAuditLogDto);
    }
}

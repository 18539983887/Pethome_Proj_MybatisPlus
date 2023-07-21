package com.qpf.pet.controller;

import com.qpf.pet.service.ISearchMasterMsgAuditLogService;
import com.qpf.pet.pojo.SearchMasterMsgAuditLog;
import com.qpf.pet.dto.SearchMasterMsgAuditLogDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/searchMasterMsgAuditLog")
public class SearchMasterMsgAuditLogController {

    @Autowired
    public ISearchMasterMsgAuditLogService searchMasterMsgAuditLogService;

    /**
     * 接口：添加或修改
     * @param searchMasterMsgAuditLog 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody SearchMasterMsgAuditLog searchMasterMsgAuditLog) {
        if ( searchMasterMsgAuditLog.getId() != null){
            searchMasterMsgAuditLogService.updateById(searchMasterMsgAuditLog);
        }else{
            searchMasterMsgAuditLogService.add(searchMasterMsgAuditLog);
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
        searchMasterMsgAuditLogService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        searchMasterMsgAuditLogService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public SearchMasterMsgAuditLog findOne(@PathVariable("id") Long id) {
        return searchMasterMsgAuditLogService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<SearchMasterMsgAuditLog> findAll() {
        return searchMasterMsgAuditLogService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param searchMasterMsgAuditLogDto 查询对象
     * @return IPage<SearchMasterMsgAuditLog> 分页对象
     */
    @PostMapping
    public IPage<SearchMasterMsgAuditLog> findByPage(@RequestBody SearchMasterMsgAuditLogDto searchMasterMsgAuditLogDto) {
        return searchMasterMsgAuditLogService.findByPage(searchMasterMsgAuditLogDto);
    }
}

package com.qpf.pet.controller;

import com.qpf.pet.pojo.SearchMasterMsgAuditLog;
import com.qpf.pet.service.ISearchMasterMsgService;
import com.qpf.pet.pojo.SearchMasterMsg;
import com.qpf.pet.dto.SearchMasterMsgDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/searchMasterMsg")
public class SearchMasterMsgController {

    @Autowired
    public ISearchMasterMsgService searchMasterMsgService;

    /**
     * 接口：添加或修改
     * @param searchMasterMsg 传递的实体
     * @return AjaxResult 响应给前端
     */
    //发布寻主消息
    @PostMapping("/publish")
    public AjaxResult publish(@RequestBody SearchMasterMsg searchMasterMsg, HttpServletRequest request){
        searchMasterMsgService.publish(searchMasterMsg,request);
        return AjaxResult.me();
    }
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody SearchMasterMsg searchMasterMsg) {
        if ( searchMasterMsg.getId() != null){
            searchMasterMsgService.updateById(searchMasterMsg);
        }else{
            searchMasterMsgService.add(searchMasterMsg);
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
        searchMasterMsgService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        searchMasterMsgService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public SearchMasterMsg findOne(@PathVariable("id") Long id) {
        return searchMasterMsgService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<SearchMasterMsg> findAll() {
        return searchMasterMsgService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param searchMasterMsgDto 查询对象
     * @return IPage<SearchMasterMsg> 分页对象
     */
    @PostMapping
    public IPage<SearchMasterMsg> findByPage(@RequestBody SearchMasterMsgDto searchMasterMsgDto) {
        return searchMasterMsgService.findByPage(searchMasterMsgDto);
    }
    //获取寻主消息列表(待审核)
    @PostMapping("/toAudit")
    public IPage<SearchMasterMsg> toAudit(@RequestBody SearchMasterMsgDto searchMasterMsgDto){
        return searchMasterMsgService.toAudit(searchMasterMsgDto);
    }
    //真正的审核
    @PutMapping("/auditResult")
    public AjaxResult auditResult(@RequestBody SearchMasterMsgAuditLog searchMasterMsgAuditLog){
        searchMasterMsgService.auditResult(searchMasterMsgAuditLog);
        return AjaxResult.me();
    }
}

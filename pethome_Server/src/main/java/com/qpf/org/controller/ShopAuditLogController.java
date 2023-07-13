package com.qpf.org.controller;

import com.qpf.org.service.IShopAuditLogService;
import com.qpf.org.pojo.ShopAuditLog;
import com.qpf.org.dto.ShopAuditLogDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/shopAuditLog")
public class ShopAuditLogController {

    @Autowired
    public IShopAuditLogService shopAuditLogService;

    /**
     * 接口：添加或修改
     * @param shopAuditLog 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody ShopAuditLog shopAuditLog) {
        if ( shopAuditLog.getId() != null){
            shopAuditLogService.updateById(shopAuditLog);
        }else{
            shopAuditLogService.add(shopAuditLog);
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
        shopAuditLogService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        shopAuditLogService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public ShopAuditLog findOne(@PathVariable("id") Long id) {
        return shopAuditLogService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<ShopAuditLog> findAll() {
        return shopAuditLogService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param shopAuditLogDto 查询对象
     * @return IPage<ShopAuditLog> 分页对象
     */
    @PostMapping
    public IPage<ShopAuditLog> findByPage(@RequestBody ShopAuditLogDto shopAuditLogDto) {
        return shopAuditLogService.findByPage(shopAuditLogDto);
    }
}

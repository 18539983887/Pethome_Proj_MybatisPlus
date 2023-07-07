package com.qpf.org.controller;

import com.qpf.org.service.IShopService;
import com.qpf.org.pojo.Shop;
import com.qpf.org.dto.ShopDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    public IShopService shopService;

    /**
     * 接口：添加或修改
     * @param shop 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Shop shop) {
        if ( shop.getId() != null){
            shopService.updateById(shop);
        }else{
            shopService.add(shop);
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
        shopService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        shopService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public Shop findOne(@PathVariable("id") Long id) {
        return shopService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<Shop> findAll() {
        return shopService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param shopDto 查询对象
     * @return IPage<Shop> 分页对象
     */
    @PostMapping
    public IPage<Shop> findByPage(@RequestBody ShopDto shopDto) {
        return shopService.findByPage(shopDto);
    }
}

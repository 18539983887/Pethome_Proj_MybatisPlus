package com.qpf.org.controller;

import com.qpf.basic.utils.ExcelUtils;
import com.qpf.org.pojo.ShopAuditLog;
import com.qpf.org.service.IShopService;
import com.qpf.org.pojo.Shop;
import com.qpf.org.dto.ShopDto;
import com.qpf.basic.vo.AjaxResult;
//import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    /**
     * 商家入驻
     * @param shop
     * @return
     */
    @PostMapping( "/settlement")
    public AjaxResult settlement(@RequestBody Shop shop) {
        shopService.settlement(shop);
        return new AjaxResult();
    }
    /**
     * 接口：店铺人工审核通过
     *
     * @param
     * @return
     */
    @PostMapping("/audit/pass")
//    @Operation(summary = "店铺审核通过")
    public AjaxResult pass(@RequestBody ShopAuditLog log) {
        try {
            shopService.pass(log);
            return new AjaxResult();//true + "操作成功"
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxResult(false, "系统繁忙，请稍后重试!!!");
        }
    }


    /**
     * 接口：店铺人工审核驳回
     *
     * @param
     * @return
     */
    @PostMapping("/audit/reject")
//    @Operation(summary = "店铺审核驳回")
    public AjaxResult reject(@RequestBody ShopAuditLog log) {
        try {
            shopService.reject(log);
            return new AjaxResult();//true + "操作成功"
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxResult(false, "系统繁忙，请稍后重试!!!");
        }
    }
    /**
     * 接口：邮件激活
     *
     * @param shopId 店铺Id
     * @return
     */
    @GetMapping("/active/{shopId}")
//    @Operation(summary = "店铺邮件激活")
    public void pass(@PathVariable Long shopId, HttpServletResponse resp) {
        try {
            Shop shop = new Shop();
            shop.setId(shopId);
            shop.setState(3);
            shopService.updateById(shop);
            resp.sendRedirect("http://127.0.0.1:8081/#/login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/importExcel")
    public AjaxResult importExcel(@RequestPart("file") MultipartFile file){
        //ExcelUtils.importExcel(文件对象, 标题行数, 表头行数, 生成的对象类型)
        List<Shop> list = ExcelUtils.importExcel(file,0,1,Shop.class);
        System.out.println("导入数据一共【"+list.size()+"】行");
        for (Shop shop:list) {
            System.out.println(shop);
        }
        //保存到数据库中[自己实现，提示：动态sql 批量插入]
        return AjaxResult.me();
    }
    @GetMapping("/export")
    public void export( HttpServletResponse response){
        List<Shop> shops = shopService.findAll();
        //ExcelUtils.exportExcel(导出的数据, 标题, 页名称, 导出的类型, 文件名, 响应对象)
        //文件名如果不写后缀默认是xls，可以写上xlsx就是指定的xlsx类型
        ExcelUtils.exportExcel(shops, null, "店铺信息", Shop.class, "shop.xlsx", response);
    }


}

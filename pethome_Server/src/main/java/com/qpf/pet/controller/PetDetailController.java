package com.qpf.pet.controller;

import com.qpf.pet.service.IPetDetailService;
import com.qpf.pet.pojo.PetDetail;
import com.qpf.pet.dto.PetDetailDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/petDetail")
public class PetDetailController {

    @Autowired
    public IPetDetailService petDetailService;

    /**
     * 接口：添加或修改
     * @param petDetail 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody PetDetail petDetail) {
        if ( petDetail.getId() != null){
            petDetailService.updateById(petDetail);
        }else{
            petDetailService.add(petDetail);
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
        petDetailService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        petDetailService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public PetDetail findOne(@PathVariable("id") Long id) {
        return petDetailService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<PetDetail> findAll() {
        return petDetailService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param petDetailDto 查询对象
     * @return IPage<PetDetail> 分页对象
     */
    @PostMapping
    public IPage<PetDetail> findByPage(@RequestBody PetDetailDto petDetailDto) {
        return petDetailService.findByPage(petDetailDto);
    }
}

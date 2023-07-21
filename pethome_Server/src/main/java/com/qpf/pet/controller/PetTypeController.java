package com.qpf.pet.controller;

import com.qpf.pet.service.IPetTypeService;
import com.qpf.pet.pojo.PetType;
import com.qpf.pet.dto.PetTypeDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/petType")
public class PetTypeController {

    @Autowired
    public IPetTypeService petTypeService;

    /**
     * 接口：添加或修改
     * @param petType 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody PetType petType) {
        if ( petType.getId() != null){
            petTypeService.updateById(petType);
        }else{
            petTypeService.add(petType);
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
        petTypeService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        petTypeService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public PetType findOne(@PathVariable("id") Long id) {
        return petTypeService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<PetType> findAll() {
        return petTypeService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param petTypeDto 查询对象
     * @return IPage<PetType> 分页对象
     */
    @PostMapping
    public IPage<PetType> findByPage(@RequestBody PetTypeDto petTypeDto) {
        return petTypeService.findByPage(petTypeDto);
    }
}

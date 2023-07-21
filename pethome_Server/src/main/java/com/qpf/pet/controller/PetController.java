package com.qpf.pet.controller;

import com.qpf.pet.service.IPetService;
import com.qpf.pet.pojo.Pet;
import com.qpf.pet.dto.PetDto;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* 后端接口类；
*/
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    public IPetService petService;

    /**
     * 接口：添加或修改
     * @param pet 传递的实体
     * @return AjaxResult 响应给前端
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Pet pet) {
        if ( pet.getId() != null){
            petService.updateById(pet);
        }else{
            petService.add(pet);
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
        petService.deleteById(id);
        return new AjaxResult();
    }

    /**
    * 接口：批量删除
    * @param ids
    * @return AjaxResult 响应给前端
    */
    @PatchMapping
    public AjaxResult patchDel(@RequestBody List<Long> ids) {
        petService.patchDel(ids);
        return new AjaxResult();
    }

    /**
    * 接口：查询单个对象
    * @param id
    */
    @GetMapping("/{id}")
    public Pet findOne(@PathVariable("id") Long id) {
        return petService.findOne(id);
    }


    /**
    * 接口：查询所有
    * @return
    */
    @GetMapping
    public List<Pet> findAll() {
        return petService.findAll();
    }


    /**
     * 接口：分页查询或高级查询
     * @param petDto 查询对象
     * @return IPage<Pet> 分页对象
     */
    @PostMapping
    public IPage<Pet> findByPage(@RequestBody PetDto petDto) {
        return petService.findByPage(petDto);
    }
}

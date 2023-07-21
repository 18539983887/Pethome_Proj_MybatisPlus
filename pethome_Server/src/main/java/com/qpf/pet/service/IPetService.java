package com.qpf.pet.service;

import com.qpf.pet.pojo.Pet;
import com.qpf.pet.dto.PetDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IPetService {
    /**
     * 添加一个对象
     * @param pet
     */
    void add(Pet pet);

    /**
     * 删除一个对象
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void patchDel(List<Long> ids);

    /**
     * 更新一个对象
     * @param pet
     */
    void updateById(Pet pet);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    Pet findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<Pet> findAll();

    /**
     * 分页查询
     * @param  petDto 分页参数
     * @return
     */
    IPage<Pet> findByPage(PetDto petDto);
}

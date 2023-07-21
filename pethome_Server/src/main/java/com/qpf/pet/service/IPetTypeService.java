package com.qpf.pet.service;

import com.qpf.pet.pojo.PetType;
import com.qpf.pet.dto.PetTypeDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IPetTypeService {
    /**
     * 添加一个对象
     * @param petType
     */
    void add(PetType petType);

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
     * @param petType
     */
    void updateById(PetType petType);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    PetType findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<PetType> findAll();

    /**
     * 分页查询
     * @param  petTypeDto 分页参数
     * @return
     */
    IPage<PetType> findByPage(PetTypeDto petTypeDto);
}

package com.qpf.pet.service;

import com.qpf.pet.pojo.PetDetail;
import com.qpf.pet.dto.PetDetailDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IPetDetailService {
    /**
     * 添加一个对象
     * @param petDetail
     */
    void add(PetDetail petDetail);

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
     * @param petDetail
     */
    void updateById(PetDetail petDetail);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    PetDetail findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<PetDetail> findAll();

    /**
     * 分页查询
     * @param  petDetailDto 分页参数
     * @return
     */
    IPage<PetDetail> findByPage(PetDetailDto petDetailDto);
}

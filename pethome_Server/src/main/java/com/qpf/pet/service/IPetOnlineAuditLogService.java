package com.qpf.pet.service;

import com.qpf.pet.pojo.PetOnlineAuditLog;
import com.qpf.pet.dto.PetOnlineAuditLogDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IPetOnlineAuditLogService {
    /**
     * 添加一个对象
     * @param petOnlineAuditLog
     */
    void add(PetOnlineAuditLog petOnlineAuditLog);

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
     * @param petOnlineAuditLog
     */
    void updateById(PetOnlineAuditLog petOnlineAuditLog);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    PetOnlineAuditLog findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<PetOnlineAuditLog> findAll();

    /**
     * 分页查询
     * @param  petOnlineAuditLogDto 分页参数
     * @return
     */
    IPage<PetOnlineAuditLog> findByPage(PetOnlineAuditLogDto petOnlineAuditLogDto);
}

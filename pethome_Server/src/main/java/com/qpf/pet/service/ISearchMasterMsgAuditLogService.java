package com.qpf.pet.service;

import com.qpf.pet.pojo.SearchMasterMsgAuditLog;
import com.qpf.pet.dto.SearchMasterMsgAuditLogDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface ISearchMasterMsgAuditLogService {
    /**
     * 添加一个对象
     * @param searchMasterMsgAuditLog
     */
    void add(SearchMasterMsgAuditLog searchMasterMsgAuditLog);

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
     * @param searchMasterMsgAuditLog
     */
    void updateById(SearchMasterMsgAuditLog searchMasterMsgAuditLog);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    SearchMasterMsgAuditLog findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<SearchMasterMsgAuditLog> findAll();

    /**
     * 分页查询
     * @param  searchMasterMsgAuditLogDto 分页参数
     * @return
     */
    IPage<SearchMasterMsgAuditLog> findByPage(SearchMasterMsgAuditLogDto searchMasterMsgAuditLogDto);
}

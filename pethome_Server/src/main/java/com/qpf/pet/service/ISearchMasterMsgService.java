package com.qpf.pet.service;

import com.qpf.pet.pojo.SearchMasterMsg;
import com.qpf.pet.dto.SearchMasterMsgDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qpf.pet.pojo.SearchMasterMsgAuditLog;

import javax.servlet.http.HttpServletRequest;


/**
* 后端Service接口；
*/
public interface ISearchMasterMsgService {
    /**
     * 添加一个对象
     * @param searchMasterMsg
     */
    void add(SearchMasterMsg searchMasterMsg);

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
     * @param searchMasterMsg
     */
    void updateById(SearchMasterMsg searchMasterMsg);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    SearchMasterMsg findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<SearchMasterMsg> findAll();

    /**
     * 分页查询
     * @param  searchMasterMsgDto 分页参数
     * @return
     */
    IPage<SearchMasterMsg> findByPage(SearchMasterMsgDto searchMasterMsgDto);

    void publish(SearchMasterMsg searchMasterMsg, HttpServletRequest request);

    IPage<SearchMasterMsg> toAudit(SearchMasterMsgDto searchMasterMsgDto);

    void auditResult(SearchMasterMsgAuditLog searchMasterMsgAuditLog);
}

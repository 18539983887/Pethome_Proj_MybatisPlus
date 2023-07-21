package com.qpf.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.pet.pojo.SearchMasterMsgAuditLog;
import com.qpf.pet.service.ISearchMasterMsgAuditLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.pet.mapper.SearchMasterMsgAuditLogMapper;
import com.qpf.pet.dto.SearchMasterMsgAuditLogDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class SearchMasterMsgAuditLogServiceImpl implements ISearchMasterMsgAuditLogService {

    @Autowired
    private SearchMasterMsgAuditLogMapper searchMasterMsgAuditLogMapper;

    @Override
    public void add(SearchMasterMsgAuditLog searchMasterMsgAuditLog){
        searchMasterMsgAuditLogMapper.insert(searchMasterMsgAuditLog);
    }

    @Override
    public void deleteById(Long id){
        searchMasterMsgAuditLogMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        searchMasterMsgAuditLogMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(SearchMasterMsgAuditLog searchMasterMsgAuditLog){
        searchMasterMsgAuditLogMapper.updateById(searchMasterMsgAuditLog);
    }

    @Override
    public SearchMasterMsgAuditLog findOne(Long id){
        return searchMasterMsgAuditLogMapper.selectById(id);
    }

    @Override
    public List<SearchMasterMsgAuditLog>findAll(){
        return searchMasterMsgAuditLogMapper.selectList(null);
    }

    @Override
    public IPage<SearchMasterMsgAuditLog>findByPage(SearchMasterMsgAuditLogDto searchMasterMsgAuditLogDto){
        //1.创建查询条件
        QueryWrapper<SearchMasterMsgAuditLog> qw = new QueryWrapper<>();
        //qw.like("xxx",searchMasterMsgAuditLogDto.getXxx());
        //qw.or();
        //qw.like("xxx",searchMasterMsgAuditLogDto.getXxx());
        //2.组件分页数据
        IPage<SearchMasterMsgAuditLog> page = new Page<>(searchMasterMsgAuditLogDto.getCurrentPage(), searchMasterMsgAuditLogDto.getPageSize());
        page.setRecords(searchMasterMsgAuditLogMapper.findByPage(page,qw));
        page.setTotal(searchMasterMsgAuditLogMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

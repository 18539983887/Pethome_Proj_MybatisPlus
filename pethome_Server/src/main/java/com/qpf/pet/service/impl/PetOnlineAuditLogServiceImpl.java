package com.qpf.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.pet.pojo.PetOnlineAuditLog;
import com.qpf.pet.service.IPetOnlineAuditLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.pet.mapper.PetOnlineAuditLogMapper;
import com.qpf.pet.dto.PetOnlineAuditLogDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class PetOnlineAuditLogServiceImpl implements IPetOnlineAuditLogService {

    @Autowired
    private PetOnlineAuditLogMapper petOnlineAuditLogMapper;

    @Override
    public void add(PetOnlineAuditLog petOnlineAuditLog){
        petOnlineAuditLogMapper.insert(petOnlineAuditLog);
    }

    @Override
    public void deleteById(Long id){
        petOnlineAuditLogMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        petOnlineAuditLogMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(PetOnlineAuditLog petOnlineAuditLog){
        petOnlineAuditLogMapper.updateById(petOnlineAuditLog);
    }

    @Override
    public PetOnlineAuditLog findOne(Long id){
        return petOnlineAuditLogMapper.selectById(id);
    }

    @Override
    public List<PetOnlineAuditLog>findAll(){
        return petOnlineAuditLogMapper.selectList(null);
    }

    @Override
    public IPage<PetOnlineAuditLog>findByPage(PetOnlineAuditLogDto petOnlineAuditLogDto){
        //1.创建查询条件
        QueryWrapper<PetOnlineAuditLog> qw = new QueryWrapper<>();
        //qw.like("xxx",petOnlineAuditLogDto.getXxx());
        //qw.or();
        //qw.like("xxx",petOnlineAuditLogDto.getXxx());
        //2.组件分页数据
        IPage<PetOnlineAuditLog> page = new Page<>(petOnlineAuditLogDto.getCurrentPage(), petOnlineAuditLogDto.getPageSize());
        page.setRecords(petOnlineAuditLogMapper.findByPage(page,qw));
        page.setTotal(petOnlineAuditLogMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

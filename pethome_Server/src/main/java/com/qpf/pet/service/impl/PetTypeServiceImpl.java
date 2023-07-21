package com.qpf.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.pet.pojo.PetType;
import com.qpf.pet.service.IPetTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.pet.mapper.PetTypeMapper;
import com.qpf.pet.dto.PetTypeDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class PetTypeServiceImpl implements IPetTypeService {

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Override
    public void add(PetType petType){
        petTypeMapper.insert(petType);
    }

    @Override
    public void deleteById(Long id){
        petTypeMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        petTypeMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(PetType petType){
        petTypeMapper.updateById(petType);
    }

    @Override
    public PetType findOne(Long id){
        return petTypeMapper.selectById(id);
    }

    @Override
    public List<PetType>findAll(){
        return petTypeMapper.selectList(null);
    }

    @Override
    public IPage<PetType>findByPage(PetTypeDto petTypeDto){
        //1.创建查询条件
        QueryWrapper<PetType> qw = new QueryWrapper<>();
        //qw.like("xxx",petTypeDto.getXxx());
        //qw.or();
        //qw.like("xxx",petTypeDto.getXxx());
        //2.组件分页数据
        IPage<PetType> page = new Page<>(petTypeDto.getCurrentPage(), petTypeDto.getPageSize());
        page.setRecords(petTypeMapper.findByPage(page,qw));
        page.setTotal(petTypeMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

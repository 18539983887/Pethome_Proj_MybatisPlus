package com.qpf.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.pet.pojo.Pet;
import com.qpf.pet.service.IPetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.pet.mapper.PetMapper;
import com.qpf.pet.dto.PetDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class PetServiceImpl implements IPetService {

    @Autowired
    private PetMapper petMapper;

    @Override
    public void add(Pet pet){
        petMapper.insert(pet);
    }

    @Override
    public void deleteById(Long id){
        petMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        petMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Pet pet){
        petMapper.updateById(pet);
    }

    @Override
    public Pet findOne(Long id){
        return petMapper.selectById(id);
    }

    @Override
    public List<Pet>findAll(){
        return petMapper.selectList(null);
    }

    @Override
    public IPage<Pet>findByPage(PetDto petDto){
        //1.创建查询条件
        QueryWrapper<Pet> qw = new QueryWrapper<>();
        //qw.like("xxx",petDto.getXxx());
        //qw.or();
        //qw.like("xxx",petDto.getXxx());
        //2.组件分页数据
        IPage<Pet> page = new Page<>(petDto.getCurrentPage(), petDto.getPageSize());
        page.setRecords(petMapper.findByPage(page,qw));
        page.setTotal(petMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

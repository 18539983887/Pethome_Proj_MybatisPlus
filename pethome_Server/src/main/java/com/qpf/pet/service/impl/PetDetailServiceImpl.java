package com.qpf.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.pet.pojo.PetDetail;
import com.qpf.pet.service.IPetDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.pet.mapper.PetDetailMapper;
import com.qpf.pet.dto.PetDetailDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class PetDetailServiceImpl implements IPetDetailService {

    @Autowired
    private PetDetailMapper petDetailMapper;

    @Override
    public void add(PetDetail petDetail){
        petDetailMapper.insert(petDetail);
    }

    @Override
    public void deleteById(Long id){
        petDetailMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        petDetailMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(PetDetail petDetail){
        petDetailMapper.updateById(petDetail);
    }

    @Override
    public PetDetail findOne(Long id){
        return petDetailMapper.selectById(id);
    }

    @Override
    public List<PetDetail>findAll(){
        return petDetailMapper.selectList(null);
    }

    @Override
    public IPage<PetDetail>findByPage(PetDetailDto petDetailDto){
        //1.创建查询条件
        QueryWrapper<PetDetail> qw = new QueryWrapper<>();
        //qw.like("xxx",petDetailDto.getXxx());
        //qw.or();
        //qw.like("xxx",petDetailDto.getXxx());
        //2.组件分页数据
        IPage<PetDetail> page = new Page<>(petDetailDto.getCurrentPage(), petDetailDto.getPageSize());
        page.setRecords(petDetailMapper.findByPage(page,qw));
        page.setTotal(petDetailMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

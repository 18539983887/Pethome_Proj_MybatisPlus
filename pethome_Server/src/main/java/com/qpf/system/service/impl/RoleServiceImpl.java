package com.qpf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.system.pojo.Role;
import com.qpf.system.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.system.mapper.RoleMapper;
import com.qpf.system.dto.RoleDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void add(Role role){
        roleMapper.insert(role);
    }

    @Override
    public void deleteById(Long id){
        roleMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        roleMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Role role){
        roleMapper.updateById(role);
    }

    @Override
    public Role findOne(Long id){
        return roleMapper.selectById(id);
    }

    @Override
    public List<Role>findAll(){
        return roleMapper.selectList(null);
    }

    @Override
    public IPage<Role>findByPage(RoleDto roleDto){
        //1.创建查询条件
        QueryWrapper<Role> qw = new QueryWrapper<>();
        //qw.like("xxx",roleDto.getXxx());
        //qw.or();
        //qw.like("xxx",roleDto.getXxx());
        //2.组件分页数据
        IPage<Role> page = new Page<>(roleDto.getCurrentPage(), roleDto.getPageSize());
        page.setRecords(roleMapper.findByPage(page,qw));
        page.setTotal(roleMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

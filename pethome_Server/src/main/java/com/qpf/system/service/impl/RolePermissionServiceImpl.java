package com.qpf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.system.pojo.RolePermission;
import com.qpf.system.service.IRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.system.mapper.RolePermissionMapper;
import com.qpf.system.dto.RolePermissionDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class RolePermissionServiceImpl implements IRolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public void add(RolePermission rolePermission){
        rolePermissionMapper.insert(rolePermission);
    }

    @Override
    public void deleteById(Long id){
        rolePermissionMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        rolePermissionMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(RolePermission rolePermission){
        rolePermissionMapper.updateById(rolePermission);
    }

    @Override
    public RolePermission findOne(Long id){
        return rolePermissionMapper.selectById(id);
    }

    @Override
    public List<RolePermission>findAll(){
        return rolePermissionMapper.selectList(null);
    }

    @Override
    public IPage<RolePermission>findByPage(RolePermissionDto rolePermissionDto){
        //1.创建查询条件
        QueryWrapper<RolePermission> qw = new QueryWrapper<>();
        //qw.like("xxx",rolePermissionDto.getXxx());
        //qw.or();
        //qw.like("xxx",rolePermissionDto.getXxx());
        //2.组件分页数据
        IPage<RolePermission> page = new Page<>(rolePermissionDto.getCurrentPage(), rolePermissionDto.getPageSize());
        page.setRecords(rolePermissionMapper.findByPage(page,qw));
        page.setTotal(rolePermissionMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

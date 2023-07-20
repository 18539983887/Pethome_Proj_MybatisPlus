package com.qpf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.system.pojo.Permission;
import com.qpf.system.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.system.mapper.PermissionMapper;
import com.qpf.system.dto.PermissionDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public void add(Permission permission){
        permissionMapper.insert(permission);
    }

    @Override
    public void deleteById(Long id){
        permissionMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        permissionMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Permission permission){
        permissionMapper.updateById(permission);
    }

    @Override
    public Permission findOne(Long id){
        return permissionMapper.selectById(id);
    }

    @Override
    public List<Permission>findAll(){
        return permissionMapper.selectList(null);
    }

    @Override
    public IPage<Permission>findByPage(PermissionDto permissionDto){
        //1.创建查询条件
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        //qw.like("xxx",permissionDto.getXxx());
        //qw.or();
        //qw.like("xxx",permissionDto.getXxx());
        //2.组件分页数据
        IPage<Permission> page = new Page<>(permissionDto.getCurrentPage(), permissionDto.getPageSize());
        page.setRecords(permissionMapper.findByPage(page,qw));
        page.setTotal(permissionMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

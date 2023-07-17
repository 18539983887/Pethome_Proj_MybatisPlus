package com.qpf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.system.pojo.RoleMenu;
import com.qpf.system.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.system.mapper.RoleMenuMapper;
import com.qpf.system.dto.RoleMenuDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class RoleMenuServiceImpl implements IRoleMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public void add(RoleMenu roleMenu){
        roleMenuMapper.insert(roleMenu);
    }

    @Override
    public void deleteById(Long id){
        roleMenuMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        roleMenuMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(RoleMenu roleMenu){
        roleMenuMapper.updateById(roleMenu);
    }

    @Override
    public RoleMenu findOne(Long id){
        return roleMenuMapper.selectById(id);
    }

    @Override
    public List<RoleMenu>findAll(){
        return roleMenuMapper.selectList(null);
    }

    @Override
    public IPage<RoleMenu>findByPage(RoleMenuDto roleMenuDto){
        //1.创建查询条件
        QueryWrapper<RoleMenu> qw = new QueryWrapper<>();
        //qw.like("xxx",roleMenuDto.getXxx());
        //qw.or();
        //qw.like("xxx",roleMenuDto.getXxx());
        //2.组件分页数据
        IPage<RoleMenu> page = new Page<>(roleMenuDto.getCurrentPage(), roleMenuDto.getPageSize());
        page.setRecords(roleMenuMapper.findByPage(page,qw));
        page.setTotal(roleMenuMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

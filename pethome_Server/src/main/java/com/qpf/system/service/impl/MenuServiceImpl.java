package com.qpf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.system.pojo.Menu;
import com.qpf.system.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.system.mapper.MenuMapper;
import com.qpf.system.dto.MenuDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public void add(Menu menu){
        menuMapper.insert(menu);
    }

    @Override
    public void deleteById(Long id){
        menuMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        menuMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Menu menu){
        menuMapper.updateById(menu);
    }

    @Override
    public Menu findOne(Long id){
        return menuMapper.selectById(id);
    }

    @Override
    public List<Menu>findAll(){
        return menuMapper.selectList(null);
    }

    @Override
    public IPage<Menu>findByPage(MenuDto menuDto){
        //1.创建查询条件
        QueryWrapper<Menu> qw = new QueryWrapper<>();
        //qw.like("xxx",menuDto.getXxx());
        //qw.or();
        //qw.like("xxx",menuDto.getXxx());
        //2.组件分页数据
        IPage<Menu> page = new Page<>(menuDto.getCurrentPage(), menuDto.getPageSize());
        page.setRecords(menuMapper.findByPage(page,qw));
        page.setTotal(menuMapper.selectCount(qw));
        //3.返回
        return page;
    }
}

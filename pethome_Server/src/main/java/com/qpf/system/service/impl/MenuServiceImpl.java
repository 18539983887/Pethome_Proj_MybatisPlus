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

import java.util.*;

/**
 * 业务实现类：
 */
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public void add(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void deleteById(Long id) {
        menuMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids) {
        menuMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Menu menu) {
        menuMapper.updateById(menu);
    }

    @Override
    public Menu findOne(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    public List<Menu> findAll() {
        return menuMapper.selectList(null);
    }

    @Override
    public IPage<Menu> findByPage(MenuDto menuDto) {
        //1.创建查询条件
        QueryWrapper<Menu> qw = new QueryWrapper<>();
        //qw.like("xxx",menuDto.getXxx());
        //qw.or();
        //qw.like("xxx",menuDto.getXxx());
        //2.组件分页数据
        IPage<Menu> page = new Page<>(menuDto.getCurrentPage(), menuDto.getPageSize());
        page.setRecords(menuMapper.findByPage(page, qw));
        page.setTotal(menuMapper.selectCount(qw));
        //3.返回
        return page;
    }

    @Override
    public List<Menu> menuTree() {
        //1.查询所有菜单
        List<Menu> allMenus = menuMapper.selectList(null);
        //2.避免双重for循环（转换为Map集合<菜单ID,菜单对象>）
        Map<Long, Menu> map = new HashMap<>();
        for (Menu menu : allMenus) {
            map.put(menu.getId(), menu);
            System.out.println(map);
        }
        //3.创建最终的菜单树集合
        List<Menu> menuTree = new ArrayList<>();
        //4.遍历Map集合
        for (Menu menu : allMenus) {
            //4.1如果是顶级部门 - 就添加到部门树
            if (menu.getParentId() == null) {
                menuTree.add(menu);
            } else {
                //4.2 如果有父级部门
                //获取当前部门的父部门id
                Long parentId = menu.getParentId();
                //根据父部门id获取父部门
                Menu m = map.get(parentId);
                //将自己添加到父部门的集合列表中
                m.getChildren().add(menu);
            }

        }
        return menuTree;
    }
}

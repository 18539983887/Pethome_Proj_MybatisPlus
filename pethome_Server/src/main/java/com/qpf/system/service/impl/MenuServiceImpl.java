package com.qpf.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
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
        }
        //3.创建最终的菜单树集合
        List<Menu> menuTree = new ArrayList<>();
        //4.遍历Map集合
        for (Menu menu : allMenus) {
            //4.1如果是顶级部门 - 就添加到部门数
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
        //5.返回菜单树
        return menuTree;
    }

    /**
     * 获取所有菜单路径
     *
     * @return 菜单路径<菜单ID, 菜单路径>
     */
    @Override
    public Map<Long, String> menuPath() {
        //1.查询所有菜单
        List<Menu> menuTree = menuTree();
        //2.定义存储菜单路径的集合
        Map<Long, String> menuPath = new HashMap<>();
        //3.遍历添加
        for (Menu menu : menuTree) {
            //3.1 添加顶级部门
            menuPath.put(menu.getId(), "/" + menu.getId());
            //3.2 添加当前顶级部门的子部门
            setFullPath(menuPath,menu.getChildren());
        }
        //4.返回
        return menuPath;
    }

    private void setFullPath(Map<Long, String> menuPath, List<Menu> childrenMenuTree) {
        //如果没有子部门，则停止
        if (ObjectUtil.isEmpty(childrenMenuTree)) {
            return;
        }
        //如果有子部门，则便利子部门
        for (Menu menu : childrenMenuTree) {
            String parentPath = menuPath.get(menu.getParentId());
            menuPath.put(menu.getId(), parentPath + "/" + menu.getId());
            setFullPath(menuPath, menu.getChildren());
        }
    }
}

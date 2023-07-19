package com.qpf.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.system.mapper.RoleMenuMapper;
import com.qpf.system.mapper.RolePermissionMapper;
import com.qpf.system.pojo.RoleMenu;
import com.qpf.system.pojo.RolePermission;
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
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Transactional
    @Override
    public void add(Role role){
        roleMapper.insert(role);
        //2.添加角色对应权限
        if (ObjectUtil.isNotEmpty(role.getPermissionIds())) {
            List<Long> permissionIds = role.getPermissionIds();
            for (Long permissionId : permissionIds) {
                rolePermissionMapper.insert(new RolePermission().setRoleId(role.getId()).setPermissionId(permissionId));
            }
        }
        //3.添加角色对应菜单
        if (ObjectUtil.isNotEmpty(role.getMenuIds())) {
            List<Long> menuIds = role.getMenuIds();
            for (Long menuId : menuIds) {
                roleMenuMapper.insert(new RoleMenu().setRoleId(role.getId()).setMenuId(menuId));
            }
        }
    }
    @Transactional
    @Override
    public void deleteById(Long id){
        //1.删除角色对应的菜单
        UpdateWrapper<RoleMenu> rmUw = new UpdateWrapper<>();
        rmUw.ge("role_id", id);
        roleMenuMapper.delete(rmUw);
        //2.删除角色对应的权限
        UpdateWrapper<RolePermission> rpUw = new UpdateWrapper<>();
        rpUw.ge("role_id", id);
        rolePermissionMapper.delete(rpUw);
        //3.删除角色
        roleMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        //1.删除角色对应的菜单
        UpdateWrapper<RoleMenu> rmUw = new UpdateWrapper<>();
        rmUw.in("role_id", ids);
        roleMenuMapper.delete(rmUw);
        //2.删除角色对应的权限
        UpdateWrapper<RolePermission> rpUw = new UpdateWrapper<>();
        rpUw.in("role_id", ids);
        rolePermissionMapper.delete(rpUw);
        //3.删除角色
        roleMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Role role){
        //1.修改角色
        roleMapper.updateById(role);
        //2.修改角色对应权限
        UpdateWrapper<RoleMenu> rmUw = new UpdateWrapper<>();
        rmUw.ge("role_id", role.getId());
        roleMenuMapper.delete(rmUw);
        if (ObjectUtil.isNotEmpty(role.getPermissionIds())) {
            List<Long> permissionIds = role.getPermissionIds();
            for (Long permissionId : permissionIds) {
                rolePermissionMapper.insert(new RolePermission().setRoleId(role.getId()).setPermissionId(permissionId));
            }
        }
        //3.修改角色对应菜单
        UpdateWrapper<RolePermission> rpUw = new UpdateWrapper<>();
        rpUw.ge("role_id", role.getId());
        rolePermissionMapper.delete(rpUw);
        if (ObjectUtil.isNotEmpty(role.getMenuIds())) {
            List<Long> menuIds = role.getMenuIds();
            for (Long menuId : menuIds) {
                roleMenuMapper.insert(new RoleMenu().setRoleId(role.getId()).setMenuId(menuId));
            }
        }
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

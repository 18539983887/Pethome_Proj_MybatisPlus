package com.qpf.system.service;

import com.qpf.system.pojo.RolePermission;
import com.qpf.system.dto.RolePermissionDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IRolePermissionService {
    /**
     * 添加一个对象
     * @param rolePermission
     */
    void add(RolePermission rolePermission);

    /**
     * 删除一个对象
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void patchDel(List<Long> ids);

    /**
     * 更新一个对象
     * @param rolePermission
     */
    void updateById(RolePermission rolePermission);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    RolePermission findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<RolePermission> findAll();

    /**
     * 分页查询
     * @param  rolePermissionDto 分页参数
     * @return
     */
    IPage<RolePermission> findByPage(RolePermissionDto rolePermissionDto);
}

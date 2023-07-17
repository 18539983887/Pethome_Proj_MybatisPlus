package com.qpf.system.service;

import com.qpf.system.pojo.Permission;
import com.qpf.system.dto.PermissionDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IPermissionService {
    /**
     * 添加一个对象
     * @param permission
     */
    void add(Permission permission);

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
     * @param permission
     */
    void updateById(Permission permission);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    Permission findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<Permission> findAll();

    /**
     * 分页查询
     * @param  permissionDto 分页参数
     * @return
     */
    IPage<Permission> findByPage(PermissionDto permissionDto);
}

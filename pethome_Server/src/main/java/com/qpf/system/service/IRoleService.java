package com.qpf.system.service;

import com.qpf.system.pojo.Role;
import com.qpf.system.dto.RoleDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IRoleService {
    /**
     * 添加一个对象
     * @param role
     */
    void add(Role role);

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
     * @param role
     */
    void updateById(Role role);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    Role findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<Role> findAll();

    /**
     * 分页查询
     * @param  roleDto 分页参数
     * @return
     */
    IPage<Role> findByPage(RoleDto roleDto);
}

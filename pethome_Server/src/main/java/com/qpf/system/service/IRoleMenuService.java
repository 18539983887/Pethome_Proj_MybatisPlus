package com.qpf.system.service;

import com.qpf.system.pojo.RoleMenu;
import com.qpf.system.dto.RoleMenuDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IRoleMenuService {
    /**
     * 添加一个对象
     * @param roleMenu
     */
    void add(RoleMenu roleMenu);

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
     * @param roleMenu
     */
    void updateById(RoleMenu roleMenu);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    RoleMenu findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<RoleMenu> findAll();

    /**
     * 分页查询
     * @param  roleMenuDto 分页参数
     * @return
     */
    IPage<RoleMenu> findByPage(RoleMenuDto roleMenuDto);
}

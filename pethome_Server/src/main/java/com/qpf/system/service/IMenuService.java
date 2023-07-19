package com.qpf.system.service;

import com.qpf.system.pojo.Menu;
import com.qpf.system.dto.MenuDto;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IMenuService {
    /**
     * 添加一个对象
     * @param menu
     */
    void add(Menu menu);

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
     * @param menu
     */
    void updateById(Menu menu);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    Menu findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<Menu> findAll();

    /**
     * 分页查询
     * @param  menuDto 分页参数
     * @return
     */
    IPage<Menu> findByPage(MenuDto menuDto);

    List<Menu> menuTree();

    Map<Long, String> menuPath();
}

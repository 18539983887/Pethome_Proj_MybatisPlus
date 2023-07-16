package com.qpf.user.service;

import com.qpf.basic.dto.LoginDto;
import com.qpf.user.pojo.Logininfo;
import com.qpf.user.dto.LoginInfoDto;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface ILogininfoService {
    /**
     * 添加一个对象
     * @param logininfo
     */
    void add(Logininfo logininfo);

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
     * @param logininfo
     */
    void updateById(Logininfo logininfo);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    Logininfo findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<Logininfo> findAll();

    /**
     * 分页查询
     * @param  logininfoDto 分页参数
     * @return
     */
    IPage<Logininfo> findByPage(LoginInfoDto logininfoDto);

    Map<String, Object> accountLogin(LoginDto loginDto);
}

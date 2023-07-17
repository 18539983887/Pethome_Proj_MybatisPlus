package com.qpf.org.service;

import com.qpf.basic.vo.AjaxResult;
import com.qpf.org.dto.WxBinderDto;
import com.qpf.org.pojo.Wxuser;
import com.qpf.org.dto.WxuserDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IWxuserService {
    /**
     * 添加一个对象
     * @param wxuser
     */
    void add(Wxuser wxuser);

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
     * @param wxuser
     */
    void updateById(Wxuser wxuser);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    Wxuser findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<Wxuser> findAll();

    /**
     * 分页查询
     * @param  wxuserDto 分页参数
     * @return
     */
    IPage<Wxuser> findByPage(WxuserDto wxuserDto);

    /**
     * 微信登录
     * @param code
     * @return
     */
    AjaxResult wechatLogin(String code);

    AjaxResult wechatBinder(WxBinderDto binderDto);
}

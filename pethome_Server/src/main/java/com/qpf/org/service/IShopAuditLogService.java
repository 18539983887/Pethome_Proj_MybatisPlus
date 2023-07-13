package com.qpf.org.service;

import com.qpf.org.pojo.ShopAuditLog;
import com.qpf.org.dto.ShopAuditLogDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IShopAuditLogService {
    /**
     * 添加一个对象
     * @param shopAuditLog
     */
    void add(ShopAuditLog shopAuditLog);

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
     * @param shopAuditLog
     */
    void updateById(ShopAuditLog shopAuditLog);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    ShopAuditLog findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<ShopAuditLog> findAll();

    /**
     * 分页查询
     * @param  shopAuditLogDto 分页参数
     * @return
     */
    IPage<ShopAuditLog> findByPage(ShopAuditLogDto shopAuditLogDto);

}

package com.qpf.org.service;

import com.qpf.org.pojo.Shop;
import com.qpf.org.dto.ShopDto;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
* 后端Service接口；
*/
public interface IShopService {
    /**
     * 添加一个对象
     * @param shop
     */
    void add(Shop shop);

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
     * @param shop
     */
    void updateById(Shop shop);

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    Shop findOne(Long id);

    /**
     * 获取所有对象
     * @return
     */
    List<Shop> findAll();

    /**
     * 分页查询
     * @param  shopDto 分页参数
     * @return
     */
    IPage<Shop> findByPage(ShopDto shopDto);
}

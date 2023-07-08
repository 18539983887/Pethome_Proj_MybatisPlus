package com.qpf.org.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.org.pojo.Shop;
import com.qpf.org.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.org.mapper.ShopMapper;
import com.qpf.org.dto.ShopDto;

import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class ShopServiceImpl implements IShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public void add(Shop shop) {
        shopMapper.insert(shop);
    }

    @Override
    public void deleteById(Long id) {
        shopMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids) {
        shopMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Shop shop) {
        shopMapper.updateById(shop);
    }

    @Override
    public Shop findOne(Long id) {
        return shopMapper.selectById(id);
    }

    @Override
    public List<Shop> findAll() {
        return shopMapper.selectList(null);
    }

    @Override
    public IPage<Shop> findByPage(ShopDto shopDto) {
        //1.创建查询条件
        QueryWrapper<Shop> qw = new QueryWrapper<>();
        qw.like("name", shopDto.getKeyword());
        qw.or();
        qw.like("tel", shopDto.getKeyword());

        IPage<Shop> page = new Page<>(shopDto.getCurrentPage(), shopDto.getPageSize());
        page.setRecords(shopMapper.findByPage(page, qw));
        page.setTotal(shopMapper.selectCount(qw));
        //3.返回
        return page;
    }

    @Transactional
    @Override
    public void settlement(Shop shop) {

    }
}

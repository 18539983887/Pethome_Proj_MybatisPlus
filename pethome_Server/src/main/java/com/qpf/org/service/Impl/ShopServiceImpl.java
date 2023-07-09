package com.qpf.org.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.basic.exception.BusinessException;
import com.qpf.org.mapper.EmployeeMapper;
import com.qpf.org.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.org.pojo.Shop;
import com.qpf.org.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.org.mapper.ShopMapper;
import com.qpf.org.dto.ShopDto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class ShopServiceImpl implements IShopService {

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

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
        //通过查询前端传过来的shop对象的admin变量是否为空，来判断是否填写的管理员
        Employee shopManager = shop.getAdmin();
        if (ObjectUtil.isEmpty(shopManager)) {
            throw new BusinessException("管理员信息不能为空！");
        }
        //判断用户名是否重复
        QueryWrapper<Employee> qw = new QueryWrapper<>();
        qw.eq("username", shopManager.getUsername());
        Employee employee = employeeMapper.selectOne(qw);
        //如果填的电话、邮箱与数据库中均不相同，说明是用户名重复
        if (employee != null) {
            if (!employee.getPhone().equals(shopManager.getPhone()) && !employee.getEmail().equals(shopManager.getEmail())) {
                throw new BusinessException("管理员账号已被占用，请重新输入！");
            }else{
                shop.setAdminId(employee.getId());
                shopManager=employee;
            }
        }else{
            //如果用户名不重复
            employeeMapper.insert(shopManager);
            System.out.println(shopManager);
            System.out.println("----------------------------");
            shop.setAdminId(shopManager.getId());
            shop.setRegisterTime(new Date());
            shop.setState(1);
            System.out.println(shop);
            System.out.println("----------------------------");
        }
        //判断用户名/手机号/邮箱是否重复
//        QueryWrapper<Employee> qw2 = new QueryWrapper<>();
//        qw2.eq("phone", shopManager.getPhone()).or().eq("email", shopManager.getEmail());
//        Employee employee2 = employeeMapper.selectOne(qw2);
//        if (employee2 != null) {//手机号/邮箱重复
//            //将数据库中查到的管理员Id，存到shop的管理员中
//            shop.setAdminId(employee.getId());
//        }
        //添加用户自动生成主键
//        employeeMapper.insert(shopManager);
//        System.out.println(shopManager);
//        System.out.println("----------------------------");
//        shop.setAdminId(shopManager.getId());
//        shop.setRegisterTime(new Date());
//        shop.setState(1);
//        System.out.println(shop);
//        System.out.println("----------------------------");
        //直接添加店铺
        shopMapper.insert(shop);
        //把店铺id关联到employee
        shopManager.setShopId(shop.getId());
        employeeMapper.updateById(shopManager);
        System.out.println(shopManager);

    }
}

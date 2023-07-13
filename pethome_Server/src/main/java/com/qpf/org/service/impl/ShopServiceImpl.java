package com.qpf.org.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.basic.exception.BusinessException;
import com.qpf.basic.service.impl.FastDfsServiceImpl;
import com.qpf.basic.utils.BaiduAuditUtils;
import com.qpf.org.mapper.EmployeeMapper;
import com.qpf.org.mapper.ShopAuditLogMapper;
import com.qpf.org.pojo.Employee;
import com.qpf.org.pojo.ShopAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.org.pojo.Shop;
import com.qpf.org.service.IShopService;
import com.qpf.org.mapper.ShopMapper;
import com.qpf.org.dto.ShopDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class ShopServiceImpl implements IShopService {

    @Autowired
    private ShopAuditLogMapper shopAuditLogMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private FastDfsServiceImpl fastDfsService;
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
        //0.使用百度AI智能审核
        if (!BaiduAuditUtils.TextCensor(shop.getName())) {
            throw new BusinessException("店铺名称不合法!!!");
        }
        System.out.println(shop.getLogo());
        byte[] imgBys = fastDfsService.downloadFile(shop.getLogo());
        if (!BaiduAuditUtils.ImgCensor(imgBys)) {
            throw new BusinessException("店铺LOGO不合法!!!");
        }
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



    //获取邮件发件人
    @Value("${spring.mail.username}")
    private String mimeMessageFrom;

    /**
     * 审核通过
     * @param shopAuditLog
     */
    @Override
    public void pass(ShopAuditLog shopAuditLog) {
        //1.修改店铺状态
        shopMapper.updateById(new Shop().setState(2).setId(shopAuditLog.getShopId()));
        //2.保存审核日志
        shopAuditLog.setState(2).setAuditTime(LocalDateTime.now());
        shopAuditLogMapper.insert(shopAuditLog);
        //3.发送激活邮件
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setFrom(mimeMessageFrom);
            helper.setSubject("【宠物之家】店铺激活邮件");
            //true - 能够再内容中编写html标签 - 会解析
            helper.setText("<h3>你的店铺已经审核通过，请<a href='http://127.0.0.1:8080/shop/active/" + shopAuditLog.getShopId()
                    + "'>点击这里</a>激活邮件</h3>", true);
            //获取店铺的店长信息
            QueryWrapper<Employee> qw = new QueryWrapper<>();
            qw.eq("shop_id",shopAuditLog.getShopId());
            Employee admin = employeeMapper.selectOne(qw);
            System.out.println(admin);
            helper.setTo(admin.getEmail());
            //发送
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new BusinessException("邮件发送失败!");
        }
    }

    /**
     * 驳回店铺申请,并发送邮件
     * @param shopAuditLog 审核日志
     */
    @Override
    public void reject(ShopAuditLog shopAuditLog) {
        //1.改状态 保存审核日志 发邮件
        shopMapper.updateById(new Shop().setState(4).setId(shopAuditLog.getShopId()));

        //2.保存审核日志
        shopAuditLog.setState(4).setAuditTime(LocalDateTime.now());
        shopAuditLogMapper.insert(shopAuditLog);

        //3.发邮件
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setFrom(mimeMessageFrom);
            helper.setSubject("【宠物之家】店铺激活失败通知");
            //true - 能够再内容中编写html标签 - 会解析
            helper.setText("<h3>你的店铺审核未通过，请<a href='http://127.0.0.1:8081/#/registerEdit/" + shopAuditLog.getShopId()
                    + "'>点击这里</a>修改入驻信息重新提交</h3>", true);
            //获取店铺的店长信息
            QueryWrapper<Employee> qw = new QueryWrapper<>();
            qw.eq("shop_id", shopAuditLog.getShopId());
            System.out.println(shopAuditLog.getShopId());
            Employee admin = employeeMapper.selectOne(qw);
            System.out.println(admin);
            helper.setTo(admin.getEmail());
            //发送
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new BusinessException("驳回邮件发送失败!");
        }
    }

}

package com.qpf.org.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.basic.exception.BusinessException;
import com.qpf.basic.service.impl.FastDfsServiceImpl;
import com.qpf.org.mapper.EmployeeMapper;
import com.qpf.org.mapper.ShopMapper;
import com.qpf.org.pojo.Employee;
import com.qpf.org.pojo.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.org.pojo.ShopAuditLog;
import com.qpf.org.service.IShopAuditLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.org.mapper.ShopAuditLogMapper;
import com.qpf.org.dto.ShopAuditLogDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class ShopAuditLogServiceImpl implements IShopAuditLogService {

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
    //获取邮件发件人
    @Value("${spring.mail.username}")
    private String mimeMessageFrom;

    @Override
    public void add(ShopAuditLog shopAuditLog){
        shopAuditLogMapper.insert(shopAuditLog);
    }

    @Override
    public void deleteById(Long id){
        shopAuditLogMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids){
        shopAuditLogMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(ShopAuditLog shopAuditLog){
        shopAuditLogMapper.updateById(shopAuditLog);
    }

    @Override
    public ShopAuditLog findOne(Long id){
        return shopAuditLogMapper.selectById(id);
    }

    @Override
    public List<ShopAuditLog>findAll(){
        return shopAuditLogMapper.selectList(null);
    }

    @Override
    public IPage<ShopAuditLog>findByPage(ShopAuditLogDto shopAuditLogDto){
        //1.创建查询条件
        QueryWrapper<ShopAuditLog> qw = new QueryWrapper<>();
        //qw.like("xxx",shopAuditLogDto.getXxx());
        //qw.or();
        //qw.like("xxx",shopAuditLogDto.getXxx());
        //2.组件分页数据
        IPage<ShopAuditLog> page = new Page<>(shopAuditLogDto.getCurrentPage(), shopAuditLogDto.getPageSize());
        page.setRecords(shopAuditLogMapper.findByPage(page,qw));
        page.setTotal(shopAuditLogMapper.selectCount(qw));
        //3.返回
        return page;
    }


}

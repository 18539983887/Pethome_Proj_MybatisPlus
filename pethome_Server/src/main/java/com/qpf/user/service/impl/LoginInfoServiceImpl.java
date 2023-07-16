package com.qpf.user.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.basic.dto.LoginDto;
import com.qpf.basic.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.user.pojo.Logininfo;
import com.qpf.user.service.ILogininfoService;
import com.qpf.user.mapper.LoginInfoMapper;
import com.qpf.user.dto.LoginInfoDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 业务实现类：
 */
@Service
public class LoginInfoServiceImpl implements ILogininfoService {

    @Autowired
    private LoginInfoMapper logininfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void add(Logininfo logininfo) {
        logininfoMapper.insert(logininfo);
    }

    @Override
    public void deleteById(Long id) {
        logininfoMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids) {
        logininfoMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Logininfo logininfo) {
        logininfoMapper.updateById(logininfo);
    }

    @Override
    public Logininfo findOne(Long id) {
        return logininfoMapper.selectById(id);
    }

    @Override
    public List<Logininfo> findAll() {
        return logininfoMapper.selectList(null);
    }

    @Override
    public IPage<Logininfo> findByPage(LoginInfoDto logininfoDto) {
        //1.创建查询条件
        QueryWrapper<Logininfo> qw = new QueryWrapper<>();
        //qw.like("xxx",logininfoDto.getXxx());
        //qw.or();
        //qw.like("xxx",logininfoDto.getXxx());
        //2.组件分页数据
        IPage<Logininfo> page = new Page<>(logininfoDto.getCurrentPage(), logininfoDto.getPageSize());
        page.setRecords(logininfoMapper.findByPage(page, qw));
        page.setTotal(logininfoMapper.selectCount(qw));
        //3.返回
        return page;
    }

    @Override
    public Map<String, Object> accountLogin(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        //一：校验 - 空值校验
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            throw new BusinessException("信息不能为空!!!");
        }
        //二：校验 - 账号校验(因为支持用户名和邮箱登录,所以条件要写多条件)
        QueryWrapper<Logininfo> qw = new QueryWrapper<>();
        qw.eq("type", loginDto.getType());
        qw.eq("username", username).or().eq("email", username).or().eq("phone", username);
        Logininfo logininfo = logininfoMapper.selectOne(qw);//登录信息
        if (logininfo == null) {
            throw new BusinessException("账号或密码错误!!!");
        }
        //三：校验 - 密码 - 加密加盐
        //前端输入的密码进行加密加盐
        String inputPwd = SecureUtil.md5(password + logininfo.getSalt());
        if (!inputPwd.equals(logininfo.getPassword())) {
            throw new BusinessException("账号或密码错误!!!");
        }
        //四：校验 - 是否被禁用
        if (!logininfo.getDisable()) {
            throw new BusinessException("该账号被禁用，请联系管理员!!!");
        }
        //五：生成token，并将登录信息保存到redis数据库，设置30有效
        String token = IdUtil.fastSimpleUUID();//36位随机字符串
        redisTemplate.opsForValue().set(token, logininfo, 30, TimeUnit.MINUTES);

        //封住返回值：Map【token，logininfo】
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        logininfo.setSalt(null);
        logininfo.setPassword(null);
        map.put("logininfo", logininfo);

        return map;
    }
}

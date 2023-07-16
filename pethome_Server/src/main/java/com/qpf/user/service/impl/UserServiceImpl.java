package com.qpf.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.basic.exception.BusinessException;
import com.qpf.user.mapper.LoginInfoMapper;
import com.qpf.user.pojo.Logininfo;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.user.pojo.User;
import com.qpf.user.service.IUserService;
import com.qpf.user.mapper.UserMapper;
import com.qpf.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 业务实现类：
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LoginInfoMapper loginInfoMapper;
    @Override
    public void add(User user) {
        //处理盐值和密码
        String salt = RandomUtil.randomString(32);
        String md5Pwd = SecureUtil.md5(user.getPassword() + salt);
        user.setSalt(salt);
        user.setPassword(md5Pwd);

        //先添加loginInfo
        Logininfo logininfo = new Logininfo();
        BeanUtil.copyProperties(user, logininfo);
        logininfo.setType(1);       //员工
        logininfo.setDisable(true); //启动
        loginInfoMapper.insert(logininfo);//获取到自增ID

        //添加user的数据
        user.setLoginInfoId(logininfo.getId());
        userMapper.insert(user);
    }

    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids) {
        userMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(User user) {
        userMapper.updateById(user);
    }

    @Override
    public User findOne(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    @Override
    public IPage<User> findByPage(UserDto userDto) {
        //1.创建查询条件
        QueryWrapper<User> qw = new QueryWrapper<>();
        //qw.like("xxx",userDto.getXxx());
        //qw.or();
        //qw.like("xxx",userDto.getXxx());
        //2.组件分页数据
        IPage<User> page = new Page<>(userDto.getCurrentPage(), userDto.getPageSize());
        page.setRecords(userMapper.findByPage(page, qw));
        page.setTotal(userMapper.selectCount(qw));
        //3.返回
        return page;
    }

    @Transactional
    @Override
    public void phoneRegister(UserDto userDto) {
        String phone = userDto.getPhone();
        String inputPhoneCode = userDto.getPhoneCode();
        String password = userDto.getPassword();
        String passwordRepeat = userDto.getPasswordRepeat();
        System.out.println(phone);
        System.out.println(inputPhoneCode);
        System.out.println(password);
        System.out.println(passwordRepeat);
        //1：校验：空值校验
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(inputPhoneCode)
                || StringUtils.isEmpty(password) || StringUtils.isEmpty(passwordRepeat)) {
            throw new BusinessException("信息不能为空!!!");
        }
        //2：校验：密码是否一致
        if (!password.equals(passwordRepeat)) {
            throw new BusinessException("两次密码不一致");
        }
        //3：校验：手机号是否重复

        //4：校验手机验证码是否过期，是否正确
        Object redisPhoneCode = redisTemplate.opsForValue().get("register:" + phone);
        if (redisPhoneCode == null) {
            throw new BusinessException("手机验证码过期或不存在，请重新获取!!!");
        }
        if (!inputPhoneCode.equals(redisPhoneCode.toString().split(":")[0])) {  // code:时间戳
            throw new BusinessException("手机验证码错误!!!");
        }
        System.out.println(redisPhoneCode.toString().split(":")[0]);
        //5：添加到数据库
        //5.1：保存到logininfo
        Logininfo logininfo = new Logininfo();
        logininfo.setUsername(phone);
        logininfo.setPhone(phone);
        String salt = IdUtil.fastSimpleUUID();      //随机盐值
        logininfo.setSalt(salt);
        String pwd = SecureUtil.md5(password + salt);   //对密码进行加盐,再加密
        logininfo.setPassword(pwd);
        logininfo.setType(1);
        logininfo.setDisable(true);
        loginInfoMapper.insert(logininfo);

        //5.2：保存到user
        User user = BeanUtil.copyProperties(logininfo, User.class, "id");
        user.setState(1);
        user.setCreateTime(LocalDateTime.now());
        user.setLoginInfoId(logininfo.getId());
        userMapper.insert(user);
    }
}

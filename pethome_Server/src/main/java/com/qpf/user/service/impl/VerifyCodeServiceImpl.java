package com.qpf.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qpf.basic.dto.SmsCodeDto;
import com.qpf.basic.exception.BusinessException;
import com.qpf.user.mapper.UserMapper;
import com.qpf.user.pojo.User;
import com.qpf.user.service.IVerifyCodeService;
import com.qpf.basic.utils.MathCodeTextCreator;
import com.qpf.basic.utils.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;


    @Override
    public String imageVerifyCode(String key) {
        // //1.获取随机验证码-普通随机字符串
        // String code = VerifyCodeUtils.generateVerifyCode(4);
        // //2.将验证码存放到redis，设置5分钟过期
        // redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        // //3.把验证码的值合并到图片，使用Base64编码。并返回base64编码的字符串
        // return VerifyCodeUtils.verifyCode(115, 40, code);

        //1.获取随机验证码-算术公式随机字符串
        String code = MathCodeTextCreator.getMathCode();
        //2.处理字符串(1+3=?@4)
        String[] strs = code.split("@");
        String mathCode = strs[0];
        String mathResult = strs[1];
        //3.将算术公式验证码的结果存放到redis，设置5分钟过期
        redisTemplate.opsForValue().set(key, mathResult, 5, TimeUnit.MINUTES);
        //4.把算术公式验证码的公式合并到图片，使用Base64编码。并返回base64编码的字符串
        return VerifyCodeUtils.verifyCode(115, 40, mathCode);
    }
    @Override
    public void phoneVerifyCode(SmsCodeDto smsCodeDto) {
        String phone = smsCodeDto.getPhone();
        String inputImageCode = smsCodeDto.getImageCode();
        String key = smsCodeDto.getImageCodeKey();
        //一：校验：空值校验 - 前端和后端都可以校验手机格式问题
        if (StrUtil.isEmpty(phone) || StrUtil.isEmpty(inputImageCode)) {
            throw new BusinessException("信息不能为空!!!");
        }

        //二：校验：手机号码是否注册过
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("phone", smsCodeDto.getPhone());
        User user = userMapper.selectOne(qw);
        if (user != null) {//根据手机号码查到了 = 手机已经注册
            throw new BusinessException("该号码已经注册过，请直接登陆!!!");
        }

        //三：校验：图形验证码 - 是否过期，是否正确
        Object redisImageCode = redisTemplate.opsForValue().get(key);
        if (redisImageCode == null) {//图形验证码过期
            throw new BusinessException("图形验证码过期!!!");
        }
        if (!inputImageCode.equalsIgnoreCase(redisImageCode.toString())) {
            throw new BusinessException("图形验证码错误!!!");
        }

        //四：直接从redis获取手机验证码：key【业务🗡:手机号】
        Object phoneCode = redisTemplate.opsForValue().get("register:" + phone);
        //1.没有获取到 = 第一次发送或者过期了，调用StrUtils工具类重新生成
        String code = null;	//用于保存手机验证码
        if (phoneCode == null) {
            code = RandomUtil.randomNumbers(4);//4位纯数字的验证码
        } else {
            //2.获取到了 = 没有过期，判断重发时间【时间戳】
            Long sendTime = Long.parseLong(phoneCode.toString().split(":")[1]); //之前的验证码设置的时间戳
            if ((System.currentTimeMillis() - sendTime) < 1 * 60 * 1000) { //1分钟重发时间
                //a.如果没有过重发时间 - 没有重发时间，居然发请求过了 = 违规操作 - 抛异
                throw new BusinessException("操作频繁，稍后重试!!!");
            } else {
                //b.如果过了重发时间 - 可以获取新的验证码【用没有过期的 - code:时间戳】
                code = phoneCode.toString().split(":")[0];
            }
        }

        //五：讲key和验证码保存在redis，重新设置过期时间3分钟【如果是用以前的-这句话就刷新了过期时间】
        redisTemplate.opsForValue().set("register:" + phone, code + ":" + System.currentTimeMillis(), 5, TimeUnit.MINUTES);
        //六：调用短信的工具类发送短信
        //SmsUtils.sendSms(phone,"你的手机验证码是：" + code +",请在3分钟内使用!!!");
        System.out.println("你的手机验证码是：" + code + ",请在5分钟内使用!!!");
    }

    @Override
    public void binderVerifyCode(SmsCodeDto smsCodeDto) {
        String phone = smsCodeDto.getPhone();
        //一：校验：空值校验 - 前端和后端都可以校验手机格式问题
        if (StrUtil.isEmpty(phone)) {
            throw new BusinessException("信息不能为空!!!");
        }

        //二：校验：手机号码是否注册过（两个手机号不能绑定一个微信）
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("phone", smsCodeDto.getPhone());
        User user = userMapper.selectOne(qw);
        if (user != null) {//根据手机号码查到了 = 手机已经注册
            throw new BusinessException("该号码已经注册过，请直接登陆!!!");
        }

        //四：直接从redis获取手机验证码：key【业务🗡:手机号】
        Object phoneCode = redisTemplate.opsForValue().get("binder:" + phone);
        //1.没有获取到 = 第一次发送或者过期了，调用StrUtils工具类重新生成
        String code = null;//用于保存手机验证码
        if (phoneCode == null) {
            code = RandomUtil.randomNumbers(4);//4位纯数字的验证码
        } else {
            //2.获取到了 = 没有过期，判断重发时间【时间戳】
            Long oldTime = Long.parseLong(phoneCode.toString().split(":")[1]); //之前的验证码设置的时间戳
            if ((System.currentTimeMillis() - oldTime) < 1 * 60 * 1000) { //1分钟重发时间
                //a.如果没有过重发时间 - 没有重发时间，居然发请求过了 = 违规操作 - 抛异
                throw new BusinessException("操作频繁，稍后重试!!!");
            } else {
                //b.如果过了重发时间 - 可以获取新的验证码【用没有过期的 - code:时间戳】
                code = phoneCode.toString().split(":")[0];
            }
        }

        //五：讲key和验证码保存在redis，重新设置过期时间3分钟【如果是用以前的-这句话就刷新了过期时间】
        redisTemplate.opsForValue().set("binder:" + phone,code+":"+System.currentTimeMillis(),3,TimeUnit.MINUTES);
        //六：调用短信的工具类发送短信
        //SmsUtils.sendSms(phone,"你的手机验证码是：" + code +",请在3分钟内使用!!!");
        System.out.println("你的手机验证码是：" + code +",请在3分钟内使用!!!");
    }
}
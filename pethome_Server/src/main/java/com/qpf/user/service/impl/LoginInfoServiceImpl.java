package com.qpf.user.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.basic.constant.WxConstants;
import com.qpf.basic.dto.LoginDto;
import com.qpf.basic.exception.BusinessException;
import com.qpf.basic.utils.HttpUtil;
import com.qpf.basic.vo.AjaxResult;
import com.qpf.org.mapper.WxuserMapper;
import com.qpf.org.pojo.Wxuser;
import com.qpf.user.mapper.UserMapper;
import com.qpf.user.pojo.User;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WxuserMapper wxuserMapper;

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

    @Override
    public AjaxResult wechatLogin(String code) {
        //1.根据code发送第二个请求：httpUtil，返回json格式字符串
        //1.1 准备第二个请求和参数
        String url = WxConstants.GET_ACK_URL.replace("APPID", WxConstants.APPID)
                .replace("SECRET", WxConstants.SECRET)
                .replace("CODE", code);
        //1.2 调用工具类发送请求，返回json格式的字符串
        String jsonStr = HttpUtil.httpGet(url);
        /*
         * {
         *   "access_token":"ACCESS_TOKEN",
         *   "expires_in":7200,
         *   "refresh_token":"REFRESH_TOKEN",
         *   "openid":"OPENID",
         *   "scope":"SCOPE",
         *   "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
         * }
         */
        //2.使用fastJson进行处理：获取token，openid
        JSONObject obj = JSONObject.parseObject(jsonStr);
        String accessToken = obj.getString("access_token");    //授权令牌
        String openid = obj.getString("openid");                //用户微信唯一标识

        //3.根据openid去查询t_wxuser表
        QueryWrapper<Wxuser> qw = new QueryWrapper<Wxuser>();
        qw.eq("openid", openid);
        Wxuser wxuser = wxuserMapper.selectOne(qw);
        //4.判断结果
        //4.1 如果微信用户查到了=以前扫过码
        if (wxuser != null) {
            //获取微信所关联的用户信息(就是User表中用户)
            User user = userMapper.selectById(wxuser.getUserId());
            //如果已经关联过User对象 - 绑定过了 - 后续扫码 - 免密登陆
            if (user != null) {
                //查询登录了信息,并返回给前台,让前台可以进行数据回显
                Logininfo logininfo = logininfoMapper.selectById(user.getLoginInfoId());
                String token = IdUtil.fastSimpleUUID(); //36位随机字符串
                redisTemplate.opsForValue().set(token, logininfo, 30, TimeUnit.MINUTES);
                //封住返回值：Map【token，logininfo】
                Map<String, Object> map = new HashMap<>();
                map.put("token", token);
                logininfo.setSalt(null);
                logininfo.setPassword(null);
                map.put("logininfo", logininfo);

                return AjaxResult.me().setResultObj(map);
            }else{
                throw new BusinessException("该用户已经不存在,请重新注册!!!");
            }
        }
        //4.2 如果没有查到(不论是有没有微信用户,还是有没有普通用户) = 响应给前端token，openid，前端跳转到绑定页面
        return AjaxResult.me().setSuccess(false).setResultObj("accessToken=" + accessToken + "&openid=" + openid);
    }
}

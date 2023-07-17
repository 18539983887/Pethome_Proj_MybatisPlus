package com.qpf.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qpf.basic.constant.WxConstants;
import com.qpf.org.dto.WxBinderDto;
import com.qpf.basic.exception.BusinessException;
import com.qpf.basic.utils.HttpUtil;
import com.qpf.basic.vo.AjaxResult;
import com.qpf.user.mapper.LoginInfoMapper;
import com.qpf.user.mapper.UserMapper;
import com.qpf.user.pojo.Logininfo;
import com.qpf.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qpf.org.pojo.Wxuser;
import com.qpf.org.service.IWxuserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qpf.org.mapper.WxuserMapper;
import com.qpf.org.dto.WxuserDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 业务实现类：
 */
@Service
public class WxuserServiceImpl implements IWxuserService {

    @Autowired
    private WxuserMapper wxuserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LoginInfoMapper logininfoMapper;

    @Override
    public void add(Wxuser wxuser) {
        wxuserMapper.insert(wxuser);
    }

    @Override
    public void deleteById(Long id) {
        wxuserMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void patchDel(List<Long> ids) {
        wxuserMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateById(Wxuser wxuser) {
        wxuserMapper.updateById(wxuser);
    }

    @Override
    public Wxuser findOne(Long id) {
        return wxuserMapper.selectById(id);
    }

    @Override
    public List<Wxuser> findAll() {
        return wxuserMapper.selectList(null);
    }

    @Override
    public IPage<Wxuser> findByPage(WxuserDto wxuserDto) {
//        //1.创建查询条件
//        QueryWrapper<Wxuser> qw = new QueryWrapper<>();
//        //qw.like("xxx",wxuserDto.getXxx());
//        //qw.or();
//        //qw.like("xxx",wxuserDto.getXxx());
//        //2.组件分页数据
//        IPage<Wxuser> page = new Page<>(wxuserDto.getCurrentPage(), wxuserDto.getPageSize());
//        page.setRecords(wxuserMapper.findByPage(page, qw));
//        page.setTotal(wxuserMapper.selectCount(qw));
//        //3.返回
        return null;
    }

    @Override
    public AjaxResult wechatLogin(String code) {
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

    /**
     * 微信绑定逻辑
     *
     * @param binderDto 微信绑定参数
     * @return
     */
    @Override
    public AjaxResult wechatBinder(WxBinderDto binderDto) {
        String phone = binderDto.getPhone();
        String verifyCode = binderDto.getVerifyCode();
        String accessToken = binderDto.getAccessToken();
        String openid = binderDto.getOpenid();
        System.out.println(accessToken + "////////////" + openid);
        //校验
        //1.校验 - 空置校验
        if (StrUtil.isEmpty(phone) || StrUtil.isEmpty(verifyCode)) {
            throw new BusinessException("数据不能为空");
        }

        //2.校验 - 验证码是否过期  //code:1215215155
        Object redisCode = redisTemplate.opsForValue().get("binder:" + phone);
        if (redisCode == null) {
            throw new BusinessException("验证码过期");
        }
        //3.校验 - 验证码是否正确
        if (!redisCode.toString().split(":")[0].equalsIgnoreCase(verifyCode.trim())) {
            throw new BusinessException("验证码错误");
        }

        //4.获取用户信息 - 发送第三个请求 - json字符串
        String url = WxConstants.GET_USER_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);

        System.out.println(url);

        String jsonStr = HttpUtil.httpGet(url);
        System.out.println(jsonStr);
        //5.解析用户信息
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        System.out.println(jsonObject);
        String openidstr = jsonObject.get("openid").toString();
        String nickname = jsonObject.get("nickname").toString();
        Integer sex = (Integer) jsonObject.get("sex");
        String address = "" + jsonObject.get("country") + jsonObject.get("province") + jsonObject.get("city");
        String headimgurl = "" + jsonObject.get("headimgurl");
        String unionid = "" + jsonObject.get("unionid");

        //6.查询手机号是否存在(同一个手机号不能绑定多个微信)
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", binderDto.getPhone()));
        if (user != null) {
            throw new BusinessException("手机号已注册,请检查后重试!");
        }

        //7.添加登录用户信息
        Logininfo logininfo = new Logininfo();
        logininfo.setUsername(binderDto.getPhone());
        logininfo.setType(1);
        logininfo.setDisable(true);
        logininfo.setPhone(binderDto.getPhone());
        String salt = IdUtil.fastSimpleUUID();
        logininfo.setSalt(salt);
        String password = SecureUtil.md5("123456" + salt);
        logininfo.setPassword(password);
        logininfoMapper.insert(logininfo);

        //8.添加用户信息
        User newUser = BeanUtil.copyProperties(logininfo, User.class, "id");
        newUser.setLoginInfoId(logininfo.getId());
        newUser.setState(1);
        newUser.setHeadImg(headimgurl);
        newUser.setCreateTime(LocalDateTime.now());
        userMapper.insert(newUser);

        //9.添加微信用户信息
        Wxuser wxuser = new Wxuser();
        wxuser.setAddress(address);
        wxuser.setHeadimgurl(headimgurl);
        wxuser.setNickname(nickname);
        wxuser.setSex(sex);
        wxuser.setOpenid(openidstr);
        wxuser.setUnionid(unionid);
        wxuser.setUserId(newUser.getId());
        wxuserMapper.insert(wxuser);

        //10.做免密登录(把数据返回给前台用来展示用户名)
        String token = IdUtil.fastSimpleUUID();
        logininfo.setPassword(null);     //去掉密码和盐值 - 安全
        logininfo.setSalt(null);
        redisTemplate.opsForValue().set(token, logininfo, 30, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("logininfo", logininfo);
        return AjaxResult.me().setResultObj(map);
    }
}

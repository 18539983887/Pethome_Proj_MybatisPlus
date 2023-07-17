package com.qpf.basic.controller;

import com.qpf.basic.dto.LoginDto;
import com.qpf.basic.vo.AjaxResult;
import com.qpf.org.dto.WxBinderDto;
import com.qpf.org.service.impl.WxuserServiceImpl;
import com.qpf.user.service.ILogininfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private ILogininfoService loginInfoService;
    @Autowired
    private WxuserServiceImpl wxuserService;

    @PostMapping("/account")
    public AjaxResult accountLogin(@RequestBody LoginDto loginDto){
        Map<String,Object> map = loginInfoService.accountLogin(loginDto);
        return AjaxResult.me().setResultObj(map);
    }
    @GetMapping("/wechat/{code}")
    public AjaxResult wechatLogin(@PathVariable("code") String code){
        try {
            return wxuserService.wechatLogin(code);
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxResult(false,"系统繁忙，请稍后重试!!!");
        }
    }
    /**
     * 微信绑定接口
     *
     * @param binderDto 微信绑定参数
     * @return
     */
    @PostMapping("/wechat/binder")
    public AjaxResult wechatBinder(@RequestBody WxBinderDto binderDto) {
        return wxuserService.wechatBinder(binderDto);
    }
}
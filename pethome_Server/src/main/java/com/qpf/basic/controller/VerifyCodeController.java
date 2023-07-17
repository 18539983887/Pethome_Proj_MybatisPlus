package com.qpf.basic.controller;

import com.qpf.basic.dto.SmsCodeDto;
import com.qpf.user.service.IVerifyCodeService;
import com.qpf.basic.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码接口
 * 1.图形验证码接口
 * 2.手机验证码接口
 */
@RestController
@RequestMapping("/verifyCode")
public class VerifyCodeController {

    @Autowired
    private IVerifyCodeService iVerifyCodeService;


    @GetMapping("/image/{key}")
    public AjaxResult imageVerifyCode(@PathVariable("key") String key){
        String base64Str = iVerifyCodeService.imageVerifyCode(key);
        return AjaxResult.me().setResultObj(base64Str); 
    }
    /**
     * 获取手机验证码接口
     * @param smsCodeDto
     * @return
     */
    @PostMapping("/smsCode")
    public AjaxResult phoneVerifyCode(@RequestBody SmsCodeDto smsCodeDto){
        iVerifyCodeService.phoneVerifyCode(smsCodeDto);
        return AjaxResult.me();
    }

    /**
     * binderSmsCode
     * @param smsCodeDto
     * @return
     */
    @PostMapping("/binderSmsCode")
    public AjaxResult binderVerifyCode(@RequestBody SmsCodeDto smsCodeDto) {
        iVerifyCodeService.binderVerifyCode(smsCodeDto);
        return AjaxResult.me();
    }
}
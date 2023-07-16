package com.qpf.basic.controller;

import com.qpf.basic.dto.LoginDto;
import com.qpf.basic.vo.AjaxResult;
import com.qpf.user.service.ILogininfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private ILogininfoService logininfoService;

    @PostMapping("/account")
    public AjaxResult accountLogin(@RequestBody LoginDto loginDto){
        Map<String,Object> map = logininfoService.accountLogin(loginDto);
        return AjaxResult.me().setResultObj(map);
    }

}
package com.qpf.user.service;

import com.qpf.basic.dto.SmsCodeDto;

public interface IVerifyCodeService {
    String imageVerifyCode(String key);

    void phoneVerifyCode(SmsCodeDto smsCodeDto);
    void binderVerifyCode(SmsCodeDto smsCodeDto);
}

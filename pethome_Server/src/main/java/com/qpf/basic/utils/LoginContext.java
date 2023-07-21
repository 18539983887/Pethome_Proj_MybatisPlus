package com.qpf.basic.utils;

import com.qpf.jwt.JwtTokenUtils;
import com.qpf.jwt.LoginData;
import com.qpf.jwt.Payload;
import com.qpf.jwt.RsaUtils;
import com.qpf.user.pojo.Logininfo;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;

/**
 * 获取当前登录人信息
 */
public class LoginContext {

    //获取登录信息对象 - 需要request
    public static Logininfo getLogininfo(HttpServletRequest request){
        //1.获取token
        String token = request.getHeader("token");
        try {
            //2.获取公钥
            PublicKey publicKey = RsaUtils.getPublicKey(RsaUtils.class.getClassLoader().getResource("auth_rsa.pub").getFile());
            //3.解密
            Payload<LoginData> payload = JwtTokenUtils.getInfoFromToken(token, publicKey, LoginData.class);
            //4.返回Logininfo对象
            return payload.getKernelData().getLogininfo();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
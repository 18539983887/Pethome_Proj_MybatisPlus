package com.qpf.basic.constant;

//微信登录相关常量
public class WxConstants {
    //微信应用的唯一标识
    public static final String APPID = "wxd853562a0548a7d0";
    //微信应用的密钥
    public static final String SECRET = "4a5d5615f93f24bdba2ba8534642dbb6";
    //请求授权url(第1次请求)
    public static final String GET_AUTH_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    //生成授权令牌url(第2次请求)
    public static final String GET_ACK_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //获取用户信息url(第3次请求)
    public static final String GET_USER_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
}
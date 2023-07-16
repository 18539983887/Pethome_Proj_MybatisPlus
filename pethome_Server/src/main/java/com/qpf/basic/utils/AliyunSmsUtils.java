package com.qpf.basic.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

/**
 * 阿里云短信工具类
 */
public class AliyunSmsUtils {

    //accessKeyId
    private static final String accessKeyId = "自己的accessKeyId";
    //子用户的accessKeySecret
    private static final String accessKeySecret = "自己的accessKeySecret";
    //短信签名
    private static final String signName = "教学项目";
    //短信模版
    private static final String templateCode = "SMS_461845481";

    /**
     * 使用AK&SK初始化账号Client
     * @return Client 阿里云短信客户端
     * @throws Exception
     */
    private static Client createClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint("dysmsapi.aliyuncs.com");
        return new Client(config);
    }

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @param phone 短信验证码
     * @return 结果信息. OK:成功, 其他失败.
     */
    public static String sendSmsCaptcha(String phone, String captcha) {
        //1.封装发送参数(手机号,签名,模版,验证码)
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phone);
        sendSmsRequest.setSignName(signName);
        sendSmsRequest.setTemplateCode(templateCode);
        sendSmsRequest.setTemplateParam("{\"code\":\"" + captcha + "\"}");
        //2.封装其他参数(默认即可)
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            //3.发送短信验证码
            Client aliyunSmsClient = AliyunSmsUtils.createClient();
            SendSmsResponseBody smsResponseBody = aliyunSmsClient.sendSmsWithOptions(sendSmsRequest, runtime).getBody();
            //{
            //  "Code":"OK"
            //	"Message":"OK",
            //	"RequestId":"2184201F-BFB3-446B-B1F2-C746B7BF0657",
            //	"BizId":"197703245997295588^0",
            //}
            return smsResponseBody.getMessage();
        } catch (Exception error) {
            return null;
        }
    }
}

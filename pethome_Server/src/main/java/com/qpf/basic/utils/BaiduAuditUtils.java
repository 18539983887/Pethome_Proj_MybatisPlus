package com.qpf.basic.utils;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 百度认证工具类：注意要修改API Key 和 Secret Key
 */
public class BaiduAuditUtils {
    /**
     * 获取权限token
     * @return 返回示例：
     * {
     * "access_token": "24.3265383f84ac64db9eff781e70587614.2592000.1654844607.282335-26205415",
     * "expires_in": 2592000
     * }
     */
    private static String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "e9qG45hTLbVDw9AyXtbsiYoz";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "iS9qYECtNGfWhd5ICfhKixhRZYopcsD3";
        return getAuth(clientId, clientSecret);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    private static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject= JSONObject.parseObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }


    public static Boolean TextCensor(String param) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        try {
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();
			//处理参数格式
            param = "text=" + param;
            //后端发送请求：通过eoken令牌将审核数据传递给url进行审核，审核完成返回数据是一个json格式字符串
            String result = HttpUtil.post(url, accessToken, param);
            //阿里巴巴的fastJson将json格式的字符串转成json对象
            JSONObject jsonObject= JSONObject.parseObject(result);
            System.err.println(jsonObject);
            //从json对象中获取属性名为conclusion的内容：合规，不合规
            String conclusion = jsonObject.getString("conclusion");
            if ("合规".equals(conclusion)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * @param bys 需要审核的图片内容
     * @return
     */
    public static Boolean ImgCensor(byte[] bys) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";
        try {
            //使用图片地址进行校验(图片的Url必须是公网IP)
            //1.图片地址进行编码
            //String imgParam = URLEncoder.encode(imageUrl, "UTF-8");
            //2.设置参数
            //String param = "imgUrl=" + imgParam;

            //使用图片内容进行校验
            //1.把图片信息,通过Base64进行编码
            String imgStr = Base64.encode(bys);
            //2.通过Url对象图片信息进行编码
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();
            String result = HttpUtil.post(url, accessToken, param);
            JSONObject jsonObject= JSONObject.parseObject(result);
            String conclusion = jsonObject.getString("conclusion");
            if ("合规".equals(conclusion)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(BaiduAuditUtils.getAuth());
        System.out.println(BaiduAuditUtils.TextCensor("操"));//false
        System.out.println(BaiduAuditUtils.TextCensor("cnm"));//false
        System.out.println(BaiduAuditUtils.TextCensor("sb"));//false
        System.out.println(BaiduAuditUtils.TextCensor("牛逼"));//true
        System.out.println(BaiduAuditUtils.TextCensor("日寇"));//true
        System.out.println(BaiduAuditUtils.TextCensor("公司"));//true
    }
}
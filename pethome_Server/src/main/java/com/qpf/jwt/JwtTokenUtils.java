package com.qpf.jwt;

import com.alibaba.fastjson.JSONObject;
import com.qpf.jwt.Payload;
import com.qpf.jwt.RsaUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

import javax.crypto.Cipher;
import java.security.*;
import java.util.UUID;

/**
 * JWT 密钥的解析和加密 工具类
 */
public class JwtTokenUtils {

    private static final String JWT_PAYLOAD_USER_KEY = "user";

    private static String createJTI() {
        return Base64.encodeBase64String(UUID.randomUUID().toString().getBytes());
    }

    /**
     * RSA公钥加密
     *
     * @param obj             被加密数据
     * @param secretPublicKey 加密公钥
     * @return 密文
     */
    private static String encryptByPublicKey(Object obj, PublicKey secretPublicKey) {
        try {
            //把数据序列化为字符串
            String jsonStr = JSONObject.toJSONString(obj);
            //如果没有加密公钥，则不加密，直接返回
            if (secretPublicKey == null) {
                return jsonStr;
            }
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, secretPublicKey);
            String outStr = Base64.encodeBase64String(cipher.doFinal(jsonStr.getBytes("UTF-8")));
            return outStr;
        } catch (Exception e) {
            throw new RuntimeException("JWT数据密钥与本地数据不匹配");
        }
    }

    /**
     * RSA私钥解密
     *
     * @param str              要解密字符串
     * @param secretPrivateKey 解密私钥
     * @return 明文Json
     */
    private static String decryptByPrivateKey(String str, PrivateKey secretPrivateKey) {
        try {
            if (secretPrivateKey == null) {
                //如果没有解密的私钥，则不需要解密，直接返回
                return str;
            }
            //开始解密
            //1.解码加密后的字符串(加密字符串时被Base64加密过的)
            byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //2.RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, secretPrivateKey);
            String outStr = new String(cipher.doFinal(inputByte));
            return outStr;
        } catch (Exception e) {
            throw new RuntimeException("JWT数据密钥与本地数据不匹配");
        }
    }

    /**
     * 生成JWT的token（数据不加密）
     *
     * @param obj            载荷中的数据
     * @param signPrivateKey 签名私钥
     * @param expire         过期时间，单位秒
     * @return JWT
     */
    public static String generateTokenExpire(Object obj, PrivateKey signPrivateKey, int expire) {
        return generateTokenExpire(obj, signPrivateKey, expire, null);
    }

    /**
     * 生成JWT的token（数据加密）
     *
     * @param obj             载荷中的数据
     * @param signPrivateKey  签名私钥
     * @param expire          过期时间，单位秒
     * @param secretPublicKey 加密公钥
     * @return JWT
     */
    public static String generateTokenExpire(Object obj, PrivateKey signPrivateKey, int expire, PublicKey secretPublicKey) {
        //对数据进行加密处理
        String str = encryptByPublicKey(obj, secretPublicKey);
        //开始封装jwt（封装数据，私钥签名）
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, str)       //填充数据
                .setId(createJTI())                     //设置ID（无意义）
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())     //设置过期时间
                .signWith(SignatureAlgorithm.RS256, signPrivateKey)             //使用私钥进行签名
                .compact();
    }

    /**
     * 获取token中的载荷信息（包括核心用户数据）
     *
     * @param token         用户请求中的令牌
     * @param signPublicKey 验签公钥
     * @param kernelType    核心数据的类型
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey signPublicKey, Class<T> kernelType) {
        return getInfoFromToken(token, signPublicKey, kernelType, null);
    }

    /**
     * 获取token中的载荷信息（包括核心用户数据）
     *
     * @param token            用户请求中的令牌
     * @param signPublicKey    验签公钥
     * @param kernelType       核心数据的类型
     * @param secretPrivateKey 解密公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey signPublicKey, Class<T> kernelType, PrivateKey secretPrivateKey) {
        //1.解析JWT的token
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signPublicKey).parseClaimsJws(token);
        //2.获取JWT中封装的数据
        Claims body = claimsJws.getBody();
        //3.解析JWT中载荷数据
        Payload<T> claims = new Payload<>();
        //3.1 封装Id
        claims.setId(body.getId());
        //3.2 封装核心数据（核心数据需要解密）
        //① 获取数据
        String bodyStr = body.get(JWT_PAYLOAD_USER_KEY).toString();
        //② 数据解密
        String json = decryptByPrivateKey(bodyStr, secretPrivateKey);
        //③ 把解密后的核心数据转换为对象
        T t = JSONObject.parseObject(json, kernelType);
        //④ 封装核心数据
        claims.setKernelData(t);
        //3.3 封装过期时间
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    /**
     * 获取token中的载荷信息（不包括用户数据）
     *
     * @param token         用户请求中的令牌
     * @param signPublicKey 验签公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey signPublicKey) {
        //1.解析JWT的token
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signPublicKey).parseClaimsJws(token);
        //2.获取JWT中封装的数据
        Claims body = claimsJws.getBody();
        //3.解析JWT中载荷数据
        Payload<T> claims = new Payload<>();
        //3.1 封装Id
        claims.setId(body.getId());
        //3.2 封装过期时间
        claims.setExpiration(body.getExpiration());
        return claims;
    }


//    public static void main(String[] args) throws Exception {
//        //1.非加密Jwt验证
//        //1.1 生成Jwt
//        PrivateKey privateKey = RsaUtils.getPrivateKey(JwtTokenUtils.class.getClassLoader().getResource("auth_rsa.pri").getFile());
//        String token = generateTokenExpire(new User(1L, "zs"), privateKey, 10);
//        System.out.println(token);
//        //1.2 解析jwt
//        PublicKey publicKey = RsaUtils.getPublicKey(JwtTokenUtils.class.getClassLoader().getResource("auth_rsa.pub").getFile());
//        Payload<User> payload = getInfoFromToken(token, publicKey, User.class);
//        System.out.println(payload);
//        Thread.sleep(11000); //超时后继续解析
//        payload = getInfoFromToken(token, publicKey, User.class);
//        System.out.println(payload);
//
        // //2.加密Jwt验证
        // //2.1 准备加密的密钥
        // String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsnen01CdQc2zh/HihCNNYI6u7AFXf/NrZ/9auPvFsJcK1cWj5EFBU3lrts2OTvrmYVurhABg2g/Ya7glzUt6DwUojHOWtpwFxSH1v7FUJMvxDsbd4GXKRdWqMkqkcCMQYDpGpshbL3IAWYIw6pgnBcKksbzkDrZCZMAyHa1bB3zh5uEm9mcrRlBUGirbPNVt++3ztIfdc4Vp5hbw++daNMFr/VGDohMVg3Dlk4ZktDgHc5nakXkE8hSr6UDTw45JpfZZ0dP9XTi/CSVQdoYD+dsJIZ8uletlbrErRfZEJNx/k0w88P4kfGteNBGhlzzVo45tMkHT33O8QB6JxI4xVQIDAQAB";
        // String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCyd6fTUJ1BzbOH8eKEI01gjq7sAVd/82tn/1q4+8WwlwrVxaPkQUFTeWu2zY5O+uZhW6uEAGDaD9hruCXNS3oPBSiMc5a2nAXFIfW/sVQky/EOxt3gZcpF1aoySqRwIxBgOkamyFsvcgBZgjDqmCcFwqSxvOQOtkJkwDIdrVsHfOHm4Sb2ZytGUFQaKts81W377fO0h91zhWnmFvD751o0wWv9UYOiExWDcOWThmS0OAdzmdqReQTyFKvpQNPDjkml9lnR0/1dOL8JJVB2hgP52wkhny6V62VusStF9kQk3H+TTDzw/iR8a140EaGXPNWjjm0yQdPfc7xAHonEjjFVAgMBAAECggEAec/qIPXZIF0CuTuEXKSr38gD5NpVmuPO38EPb0uJ96pgnuCzqMxRhmRN/Qv4ojfmn3UucH7BnJVMJtoeEy39NdtTfeo3aJS963vufNTQlf0NoARk1RElKt1XudPwwQlt2ABu0M/YTV4GlxGhyb3ohKoCN76x+si0MIhurIryovyabZCtlhGD2fg3V1t8RBlCEuz68FtB9fSh4zk7u6RhAL5LCOGNbVAiY4hx/NhrDiBfvQBhJZmPG+3gWjjZFgZEH5B0tGByuG2M+dj2qT5LFepkhGyI/upJwOhJrjiRrvR7LmSYCz0lI8/2fVCF8jN/TJav/1xVR82d/165Movm0QKBgQDZv/H1bhPoCoeh8z3ww8Um5FjFb1MMjmh4oB1d2+0QlYTbSVx6mOxBoh0yk+jKztotJyWs61nnHekOhdHFx9Ij1L8oMxybBK+heTuzl2WIs9/2CRBV4XfKMwNiYxJYkaXxUgeHx/2IVXTuFMKMrWhf7kxk2iFrK+Gv63oY0dmvhwKBgQDR0TWXRXC2qEqH/NV/6d24UHl4i/+UP1aKE9jA8xArJYBlKtTWCgM7g3/wxr0IRB6RocVupop/kZJ9RUFjprfaykDOj+A0oC+IDwUmGIjGbR4P921qjWEVQGIFSJvnIwHwGfEAPxvw0uW2tqz9C2GUZ9OB17lecfIdeJQX2Hb3QwKBgA9bWCclAkZlJ7emPgIS7H6XsCMMfODv0jJfqHKMJiX7RYlpnRoQWukuE70TbWGQQRbaIfAWERsZouwhR/AY7ZsVT/33zNap9/D9adZ6oPCJLwxdC0fjRN1/x4dS0WJpszhXvqw20Iyi6kI4OJhPSoMpfT3HnH/AcoRDqTLC6gVVAoGBAI3f9GfseZHZbERV75wF7HoEWI7tw41f4smNMAUQln9GZXKDKtXsgVEN00ZhbFMZlL4O8GyoyoAGVFLGsLeMdUfJeVbzrLyJEHrlBStEbcAW6rwLJ/5jySDQnzdJaLo7TsUnFXKAOgl24gPRtFmLB5mNN1TWJS86x2esMB+LrK33AoGADEDHIUtulc4zclLH9MJj7JcZPkgVz5llJ1jQj3fOu4iPc9TNvV2gV2kWU446gyMmRrQ2We1awnrjaeSzeFnf0OhL+yTzNUmRLMYWZhja/KMhr7b9vVRCCrysZJod+MWodEH+HIJlu9RGIxv7fNNy1S4yRU92OQU43XQ1S93eaEE=";
        // //2.2 获取Jwt的token
        // PrivateKey signPrivateKey = RsaUtils.getPrivateKey(JwtTokenUtils.class.getClassLoader().getResource("auth_rsa.pri").getFile());
        // PublicKey secretPublicKey = RsaUtils.getPublicKey(publicKeyStr.getBytes(StandardCharsets.UTF_8));
        // String token = generateTokenExpire(new User(1L, "zs"), signPrivateKey, 10, secretPublicKey);
        // System.out.println(token);
        // //2.3 解析Jwt的token里面内容
        // PublicKey signPublicKey = RsaUtils.getPublicKey(JwtTokenUtils.class.getClassLoader().getResource("auth_rsa.pub").getFile());
        // PrivateKey secretPrivateKey = RsaUtils.getPrivateKey(privateKeyStr.getBytes(StandardCharsets.UTF_8));
        // Payload<User> payload = getInfoFromToken(token, signPublicKey, User.class, secretPrivateKey);
        // System.out.println(payload);
//    }

}
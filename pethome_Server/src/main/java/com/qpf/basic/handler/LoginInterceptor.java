package com.qpf.basic.handler;

import com.qpf.jwt.JwtTokenUtils;
import com.qpf.jwt.LoginData;
import com.qpf.jwt.Payload;
import com.qpf.jwt.RsaUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

/**
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //1.获取token
//        String token = request.getHeader("token");
//        //2.判断 - 如果有token
//        if (token != null) {
//            //2.1 通过token获取redis的登录信息
//            Object obj = redisTemplate.opsForValue().get(token); //Logininfo对象
//            //2.2 如果登录信息不为null, 表示登录成功，而且没有过期
//            if (obj != null) {
//                //2.3 放行 + 刷新过期时间[重新添加到redis]
//                redisTemplate.opsForValue().set(token, obj, 30, TimeUnit.MINUTES);
//                return true;
//            }
//        }
        //1.获取请求头中的token

        //1.获取请求头中的token
        String token = request.getHeader("token");
        if (token != null) {
            //2.获取公钥
            PublicKey publicKey = RsaUtils.getPublicKey(JwtTokenUtils.class.getClassLoader().getResource("auth_rsa.pub").getFile());
            try {
                //3.使用工具类解密获取载荷PayLoad
                Payload<LoginData> payload = JwtTokenUtils.getInfoFromToken(token, publicKey, LoginData.class);
                //如果没有报错 => 登录了
                return true;//没有刷新过期时间 - 没有做
            } catch (ExpiredJwtException e) {
                //告诉浏览器：我给你响应一个json数据，并且使用UTF-8进行编码
                response.setContentType("application/json;charset=UTF-8");
                //由于上面已经告诉了浏览器指定的类型。所以浏览器接收到这个数据之后就会自动转换成json对象
                response.getWriter().println("{\"success\":false,\"msg\":\"noLogin\"}");
                return false;
            }
        }

        //3.判断 - 如果没有token/登录信息过期了(null) - 直接拦截 响应前端 - 跳转到登录页面
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println("{\"success\":false,\"message\":\"noLogin\"}");
        return false;
    }
}
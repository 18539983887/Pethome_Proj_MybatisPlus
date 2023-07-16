package com.qpf.basic.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        //1.获取token
        String token = request.getHeader("token");
        //2.判断 - 如果有token
        if (token != null) {
            //2.1 通过token获取redis的登录信息
            Object obj = redisTemplate.opsForValue().get(token); //Logininfo对象
            //2.2 如果登录信息不为null, 表示登录成功，而且没有过期
            if (obj != null) {
                //2.3 放行 + 刷新过期时间[重新添加到redis]
                redisTemplate.opsForValue().set(token, obj, 30, TimeUnit.MINUTES);
                return true;
            }
        }

        //3.判断 - 如果没有token/登录信息过期了(null) - 直接拦截 响应前端 - 跳转到登录页面
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println("{\"success\":false,\"message\":\"noLogin\"}");
        return false;
    }
}
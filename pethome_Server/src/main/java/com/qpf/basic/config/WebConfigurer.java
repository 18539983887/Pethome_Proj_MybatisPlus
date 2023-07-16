package com.qpf.basic.config;

import com.qpf.basic.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 注册SpringMVC拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")      //拦截所有请求
                .excludePathPatterns("/login/**")   //放行跟登录有关的请求
                .excludePathPatterns("/user/register/**", "/verifyCode/**")   //放行用户注册和验证码
                .excludePathPatterns("/shop/settlement", "/fastDfs/**")       //放行店铺入住和fastDfs
                .excludePathPatterns("/shop/active/**")        //放行店铺邮箱激活
                .excludePathPatterns("/doc.html", "/webjars/**", "/swagger-resources", "/v2/api-docs");   //放行swagger

        //有邮箱激活或注册,也需要放行.
    }
}
package com.qpf.basic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1) 允许的域,不要写/，否则cookie就无法使用了
//        config.addAllowedOrigin("http://127.0.0.1:8081");
//        config.addAllowedOrigin("http://localhost:8081");
//        config.addAllowedOrigin("http://127.0.0.1:80");
//        config.addAllowedOrigin("http://localhost:80");
//        //localhost:80通常指的是本地计算机上的Web服务器，通过该地址可以访问本地计算机上运行的网站或应用程序
//        config.addAllowedOrigin("http://127.0.0.1");
//        config.addAllowedOrigin("http://localhost");
//        config.addAllowedOrigin("http://127.0.0.1:63342");
//        config.addAllowedOrigin("http://localhost:63342");
        config.addAllowedOrigin("*");

        //2) 是否允许发送Cookie信息
        config.setAllowCredentials(true);
        //3) 允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        // 4）允许的头信息
        config.addAllowedHeader("*");

        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new
                UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}

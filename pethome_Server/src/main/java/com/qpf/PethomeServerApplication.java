package com.qpf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.qpf.*.mapper")
@EnableCaching
//加载Listener - 本来监听器只要服务器一启动就会执行，但是SpringBoot项目中是通过启动类开启服务的，所以要加这个注解去加载listener，listener才会起作用
@ServletComponentScan(value = {"com.qpf.system.listener"})
public class PethomeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PethomeServerApplication.class, args);
    }

}

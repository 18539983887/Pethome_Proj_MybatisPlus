package com.qpf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.qpf.*.mapper")
@EnableCaching
public class PethomeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PethomeServerApplication.class, args);
    }

}

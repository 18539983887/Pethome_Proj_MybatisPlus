package com.qpf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qpf.*.mapper")
public class PethomeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PethomeServerApplication.class, args);
    }

}

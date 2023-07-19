package com.qpf.system.annotion;

import java.lang.annotation.*;

@Target({ElementType.METHOD})//注解能作用在方法上、类上
@Retention(RetentionPolicy.RUNTIME)//可以通过反射读取注解
@Inherited//可以被继承
@Documented//可以被javadoc工具提取成文档，可以不加
public @interface PreAuthorize {
    //对应t_permission表中的sn
    String sn();
	//对应t_permission表中的name
    String name();
}
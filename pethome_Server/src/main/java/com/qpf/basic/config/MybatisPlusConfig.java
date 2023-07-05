package com.qpf.basic.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    // @Bean
    // public MybatisPlusInterceptor mpInterceptor() {
    //     //1.定义MybatisPlus拦截器
    //     MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
    //     //2.添加拦截器插件(分页插件)
    //     mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    //     //3.添加拦截器插件(乐观锁插件)
    //     mpInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    //
    //     //返回MybatisPlus拦截器
    //     return mpInterceptor;
    // }


    //V3.4以前乐观锁插件
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
    //V3.4以前分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDbType(DbType.MYSQL);
        return paginationInterceptor;
    }
}

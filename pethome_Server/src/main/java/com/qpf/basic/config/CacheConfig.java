package com.qpf.basic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 默认缓存key生成方法。一般不用(因为删除时需要明确key)。
     * key格式：类名.方法名:参数
     */
//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        return (o, method, objects) -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append(o.getClass().getName()).append(".");
//            sb.append(method.getName()).append(":");
//            if (objects != null && objects.length != 0) {
//                sb.append(":");
//                for (Object obj : objects) {
//                    sb.append(obj.toString());
//                }
//            }
//            System.out.println("keyGenerator=" + sb.toString());
//            return sb.toString();
//        };
//    }

    /**
     * 指定默认的缓存解析器（非必须）
     */
    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager);
    }

    /**
     * 指定默认缓存异常处理器（非必须）。
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        // 用于捕获从Cache中进行CRUD时的异常的回调处理器。
        return new SimpleCacheErrorHandler();
    }

    /**
     * Redis缓存管理器（非必须）。
     * 该配置主要用来更改缓存值的序列化方式，默认使用JDK的序列化。
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() //不允许空值
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }
}
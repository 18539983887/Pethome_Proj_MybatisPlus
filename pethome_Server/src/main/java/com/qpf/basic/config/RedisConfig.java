package com.qpf.basic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 自定义RedisTemplate,主要用于修改"序列化模式"
     * 注意: 不修改也没问题,但会导致在Redis客户端中"阅读不方便"
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        // //开启Redis的单机事务(一般不开启，因为Redis大多数是集群)
        // redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(factory);
        //设置Redis的键的序列化模式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        //设置Redis中Value的序列化模式(必须有JackSon包才行)
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }
}
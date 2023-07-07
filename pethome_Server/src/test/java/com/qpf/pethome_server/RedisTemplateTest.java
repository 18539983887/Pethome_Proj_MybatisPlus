package com.qpf.pethome_server;

import com.qpf.org.pojo.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
@SpringBootTest
public class RedisTemplateTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void test01(){
        Shop shop=new Shop();
        shop.setName("店铺名aaa");
        redisTemplate.boundValueOps("testRT").set(shop);
    }
}

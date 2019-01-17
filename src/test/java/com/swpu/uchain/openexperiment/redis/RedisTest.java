package com.swpu.uchain.openexperiment.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    private RedisService redisService;

    @Test
    public void setAndGet(){
        redisService.set(VerifyCodeKey.getByClientIp, "123456", "test");
        String s = redisService.get(VerifyCodeKey.getByClientIp, "123456", String.class);
        System.out.println(s);
    }
}

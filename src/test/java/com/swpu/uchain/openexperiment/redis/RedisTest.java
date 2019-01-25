package com.swpu.uchain.openexperiment.redis;

import com.alibaba.fastjson.JSONArray;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.redis.key.UserKey;
import com.swpu.uchain.openexperiment.redis.key.VerifyCodeKey;
import com.swpu.uchain.openexperiment.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisTest {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;

    @Test
    public void setAndGet(){
        redisService.set(VerifyCodeKey.getByClientIp, "123456", "test");
        String s = redisService.get(VerifyCodeKey.getByClientIp, "123456", String.class);
        System.out.println(s);
    }

    @Test
    public void testJedisArray(){
        List<User> users = userService.selectByKeyWord(1 + "");
        String jsonStr = JSONArray.toJSONString(users);
        log.info(jsonStr);
        redisService.set(UserKey.getByKeyWord, 1 + "",users);
        List<User> arraylist = redisService.getArraylist(UserKey.getByKeyWord, 1 + "", User.class);
        log.info("user.name:{}", arraylist.get(0).getRealName());
    }
}

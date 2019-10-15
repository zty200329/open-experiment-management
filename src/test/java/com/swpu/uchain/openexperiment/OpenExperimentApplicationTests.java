package com.swpu.uchain.openexperiment;

import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.AnnouncementKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenExperimentApplicationTests {

    @Autowired
    private RedisService redisService;

    @Test
    public void contextLoads() {
        redisService.set(new AnnouncementKey("123"),"hello","world");
    }

}


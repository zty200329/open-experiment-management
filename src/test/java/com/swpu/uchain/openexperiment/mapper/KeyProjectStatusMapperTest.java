package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.TimeLimit;
import com.swpu.uchain.openexperiment.enums.TimeLimitType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class KeyProjectStatusMapperTest {

    @Autowired
    private TimeLimitMapper timeLimitService;

    @Test
    public void updateList() {

        TimeLimit timeLimit = timeLimitService.getTimeLimitByTypeAndCollege(1, 6);
        //不在时间范围内
        System.out.println(timeLimit.getEndTime()+"  "+timeLimit.getStartTime() + "   "+ new Date());
    }
}
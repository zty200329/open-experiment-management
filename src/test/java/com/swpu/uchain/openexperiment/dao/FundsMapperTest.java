package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.Funds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FundsMapperTest {

    @Autowired
    private FundsMapper fundsMapper;

    @Test
    public void multiInsert() {

        List<Funds> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Funds funds = new Funds();
            funds.setUse("测试原因");
            funds.setType(1);
            funds.setAmount(2314.12F);
            funds.setId((long) i);
            list.add(funds);
        }
        fundsMapper.multiInsert(list);
    }
}
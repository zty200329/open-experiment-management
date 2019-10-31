package com.swpu.uchain.openexperiment.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KeyProjectStatusMapperTest {

    @Autowired
    private KeyProjectStatusMapper keyProjectStatusMapper;

    @Test
    public void updateList() {

        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(8L);
        Integer status = 0;
        keyProjectStatusMapper.updateList(list,status);

    }
}
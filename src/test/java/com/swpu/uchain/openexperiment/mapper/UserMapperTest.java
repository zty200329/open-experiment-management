package com.swpu.uchain.openexperiment.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void deleteByPrimaryKey() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void selectByPrimaryKey() {
    }

    @Test
    public void selectAll() {
    }

    @Test
    public void updateByPrimaryKey() {
    }

    @Test
    public void selectByUserCode() {
    }

    @Test
    public void selectProjectJoinedUsers() {
    }

    @Test
    public void selectByRandom() {
    }

    @Test
    public void selectGroupLeader() {
        System.out.println(userMapper.selectGroupLeader(1l));
    }
}
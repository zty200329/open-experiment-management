package com.swpu.uchain.openexperiment.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OperationRecordMapperTest {

    @Autowired
    private OperationRecordMapper recordMapper;

    @Test
    public void deleteByPrimaryKey() {
    }

    @Test
    public void insert() {

    }

    @Test
    public void selectAllByProjectIdList() {

        List<Long> list = new LinkedList<>();
        list.add(1L);
        list.add(4L);
        System.err.println(recordMapper.selectAllByProjectIdList(list));
    }

    @Test
    public void setNotVisibleByProjectId() {
        recordMapper.setNotVisibleByProjectId(1L,1);
    }

    @Test
    public void updateByPrimaryKey() {
    }
}
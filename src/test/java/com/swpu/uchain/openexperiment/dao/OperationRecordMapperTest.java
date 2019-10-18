package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.DTO.OperationRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

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

        List<OperationRecord> list = new ArrayList<>();
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setOperationContent("1");
        operationRecord.setOperationReason("满足要求");
        operationRecord.setOperationType("1");
        operationRecord.setRelatedId(1L);
        list.add(operationRecord);
        System.err.println(recordMapper.multiInsert(list));

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
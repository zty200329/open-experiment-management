package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageRecordMapperTest {

    @Autowired
    private MessageRecordMapper messageRecordMapper;

    @Test
    public void insert() {

        Message message = new Message();
        message.setMessageContent("测试");
        message.setUserId(11223L);
        System.err.println(messageRecordMapper.insert(message));

    }
}
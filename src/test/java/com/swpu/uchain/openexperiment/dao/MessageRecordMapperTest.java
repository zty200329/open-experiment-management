package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.Message;
import com.swpu.uchain.openexperiment.domain.RoleAcl;
import com.swpu.uchain.openexperiment.domain.UserRole;
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

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleAclMapper roleAclMapper;

    @Test
    public void insert() {

        Message message = new Message();
        message.setTitle("测试");
        message.setUserId(11223L);
        System.err.println(messageRecordMapper.insert(message));

    }

    @Test
    public void insert2(){
        RoleAcl roleAcl = new RoleAcl();
        roleAcl.setRoleId(7L);
        for (int i = 1; i < 100; i++) {
            roleAcl.setAclId((long) i);
            roleAclMapper.insert(roleAcl);
        }
    }
}
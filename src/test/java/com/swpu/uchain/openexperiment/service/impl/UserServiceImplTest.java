package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.enums.UserType;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import com.swpu.uchain.openexperiment.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProjectService userProjectService;

    @Test
    public void createUserJoin() {

        String[] usersCodeArr = new String[]{"1","21"};
        Long projectGroupId = 1L;
        userProjectService.addTeacherJoin(usersCodeArr,projectGroupId);
    }
}
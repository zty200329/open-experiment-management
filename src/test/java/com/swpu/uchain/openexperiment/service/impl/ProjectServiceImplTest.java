package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectServiceImplTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRoleService userRoleService;

    @Test
    public void valid(){
       boolean res =  userRoleService.validContainsUserRole(RoleType.NORMAL_STU);
       log.info(String.valueOf(res));
    }
    @Test
    public void getProjectDetailById() {

        System.err.println(projectService.getProjectDetailById(4L));

    }
}
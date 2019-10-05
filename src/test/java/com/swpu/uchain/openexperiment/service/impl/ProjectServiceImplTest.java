package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectServiceImplTest {

    @Autowired
    private ProjectService projectService;

    @Test
    public void getProjectDetailById() {

        System.err.println(projectService.getProjectDetailById(4L));

    }
}
package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.enums.CollegeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectGroupMapperTest {

    @Autowired
    private ProjectGroupMapper projectGroupMapper;

    @Test
    public void getAllOpenTopic() {
        System.err.println(projectGroupMapper.getAllOpenTopic());
    }

    @Test
    public void selectByUserIdAndStatus(){
        System.err.println(projectGroupMapper.selectByUserIdAndStatus(21L,0));
    }

    @Test
    public void getProjectGroupDetailVOByProjectId(){
        System.err.println(projectGroupMapper.getProjectGroupDetailVOByProjectId(1L));
    }

    @Test
    public void getProjectTableInfoListByCollege() {
        System.err.println(projectGroupMapper.getProjectTableInfoListByCollege(CollegeType.COMPUTER_SCIENCE_COLLEGE.getValue()));
    }
}
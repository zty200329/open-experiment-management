package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectGroupMapperTest {

    @Autowired
    private ProjectGroupMapper projectGroupMapper;

    @Test
    public void getAllOpenTopic() {
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(5);
        list.add(4);
        list.add(3);
        System.err.println(projectGroupMapper.getAllOpenTopic(list));
    }

    @Test
    public void selectByUserIdAndStatus(){
        System.err.println(projectGroupMapper.selectByUserIdAndStatus(21L,0,null));
    }

    @Test
    public void getProjectGroupDetailVOByProjectId(){
        System.err.println(projectGroupMapper.getProjectGroupDetailVOByProjectId(1L));
    }


    @Test
    public void updateProjectStatusOfList(){
        List<Long> list = new LinkedList<>();
        list.add(21L);
        list.add(22L);
        projectGroupMapper.updateProjectStatusOfList(list,2);
    }

    @Test
    public void conditionQuery(){
        QueryConditionForm form = new QueryConditionForm();
        form.setProjectName("系统");
        form.setLimitGrade("2020");
        System.err.println(projectGroupMapper.conditionQuery(form));
    }
}
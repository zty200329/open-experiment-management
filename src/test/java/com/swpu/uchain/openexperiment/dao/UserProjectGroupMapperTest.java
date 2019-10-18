package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.form.user.TeacherMember;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProjectGroupMapperTest {

    @Autowired
    private UserProjectGroupMapper userProjectGroupMapper;

    @Test
    public void updateTeacherTechnicalRole() {
        List<TeacherMember> list = new LinkedList<>();
        TeacherMember teacherMember = new TeacherMember();
        teacherMember.setName("测试");
        teacherMember.setTechnicalRole("数据库设计指导");
        teacherMember.setUserId(31L);
        list.add(teacherMember);
        TeacherMember teacherMember2 = new TeacherMember();
        teacherMember2.setTechnicalRole("数据库设计指导2");
        teacherMember2.setUserId(32L);
        list.add(teacherMember2);
    }
}
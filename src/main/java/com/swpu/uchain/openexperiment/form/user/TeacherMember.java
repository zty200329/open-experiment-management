package com.swpu.uchain.openexperiment.form.user;

import lombok.Data;

@Data
public class TeacherMember {

    /**
     * 教师姓名
     */
    private String name;

    /**
     * 教师工号
     */
    private Long userId;

    /**
     * 技术职称
     */
    private String technicalRole;

}

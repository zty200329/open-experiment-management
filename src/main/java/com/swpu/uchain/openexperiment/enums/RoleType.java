package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @author panghu
 */

@Getter
public enum RoleType {
    /**
     *
     */
    NORMAL_STU(1,"学生"),
    PROJECT_LEADER(2,"学生(项目组长)"),
    MENTOR(3,"指导教师"),
    LAB_ADMINISTRATOR(4,"实验室主任"),
    SECONDARY_UNIT(5,"二级单位(学院领导)"),
    FUNCTIONAL_DEPARTMENT(6,"职能部门"),
    FUNCTIONAL_DEPARTMENT_LEADER(7,"职能部门领导"),
    COLLEGE_FINALIZATION_REVIEW(9,"学院结题评审老师"),
    POSTGRADUATE(10,"研究生"),
    COLLEGE_PROJECT_REVIEW(11,"学院立项评审老师");


    private Integer value;

    private String tips;

    RoleType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }

    }

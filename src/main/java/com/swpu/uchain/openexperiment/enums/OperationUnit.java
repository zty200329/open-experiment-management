package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @author dengg
 */
@Getter
public enum OperationUnit {

    /**
     *
     */
    MENTOR(3,"指导教师"),
    LAB_ADMINISTRATOR(4,"实验室主任"),
    SECONDARY_UNIT(5,"二级单位(学院领导)"),
    FUNCTIONAL_DEPARTMENT(6,"职能部门"),
    COLLEGE_REVIEWER(9,"学院结题审核人"),
    COLLEGE_PROJECT_REVIEW(11,"学院立项审核人");

    private Integer value;

    private String tips;

    OperationUnit(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

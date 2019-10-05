package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description: 项目进度状态
 */
@Getter
public enum ProjectStatus {

    /**
     *
     */
    ESTABLISH_FAILED(-2,"立项失败"),

    REJECT_MODIFY(-1,"驳回修改"),

    DECLARE(0,"申报"),

    LAB_ALLOWED(1,"实验室审核通过,待上报"),

    LAB_ALLOWED_AND_REPORTED(2,"实验室审核通过并已上报"),

    SECONDARY_UNIT_ALLOWED(3,"二级单位审核通过,待上报"),

    SECONDARY_UNIT_ALLOWED_AND_REPORTED(4,"二级单位审核通过并已上报"),

    ESTABLISH(5,"职能部门审核通过,立项"),

    MID_TERM_INSPECTION(6,"中期检查"),

    CONCLUDED(7,"结题项目"),
    ;

    private Integer value;

    private String tips;

    ProjectStatus(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

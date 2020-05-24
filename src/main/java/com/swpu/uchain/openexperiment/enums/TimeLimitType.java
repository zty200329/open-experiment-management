package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @author dengg
 */

@Getter
public enum  TimeLimitType {
    /**
     *
     */
    DECLARE_LIMIT(0,"申报时间限制"),
    JOIN_APPLY_LIMIT(1,"学生申请加入项目时间限制"),
    AGREE_JOIN_LIMIT(2,"审批学生申请时间限制"),
    LAB_CHECK_LIMIT(3,"实验室审核时间限制"),
    KEY_DECLARE_LIMIT(4,"重点项目申报时间限制"),
    TEACHER_KEY_CHECK_LIMIT(5,"指导老师重点项目审核时间限制"),
    LAB_KEY_CHECK_LIMIT(6,"实验室重点项目审核时间限制"),
    LAB_REPORT_LIMIT(7,"实验室上报时间限制"),
    SECONDARY_UNIT_CHECK_LIMIT(8,"二级单位审核时间限制"),
    SECONDARY_UNIT_REPORT_LIMIT(20,"二级单位上报时间限制"),
    UPLOADING_INFORMATION(10,"结题资料上传时间限制");


    private Integer value;

    private String tips;

    TimeLimitType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }

}

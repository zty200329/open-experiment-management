package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

@Getter
public enum  TimeLimitType {
    /**
     *
     */
    DECLARE_LIMIT(1,"申报时间限制"),
    JOIN_APPLY_LIMIT(2,"学生申请加入项目时间限制"),
    AGREE_JOIN_LIMIT(3,"审批学生申请时间限制"),
    LAB_CHECK_LIMIT(4,"实验室审核时间限制"),
    KEY_DECLARE_LIMIT(5,"重点项目申报时间限制"),
    LAB_REPORT_LIMIT(6,"重点项目申报时间限制"),
    SECONDARY_UNIT_CHECK_LIMIT(7,"重点项目申报时间限制"),
    SECONDARY_UNIT_REPORT_LIMIT(8,"重点项目申报时间限制");


    private Integer value;

    private String tips;

    TimeLimitType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }

}

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
    KEY_PROJECT_APPLY(-4,"重点项目申请"),

    ESTABLISH_FAILED(-3,"立项失败"),

    REJECT_MODIFY(-2,"驳回修改"),

    TO_DE_CONFIRMED(-1,"待确认--项目组长编写后"),

    GUIDE_TEACHER_ALLOWED(0,"指导老师确认"),

    DECLARE(0,"申报"),

    LAB_ALLOWED(1,"实验室拟题审核通过"),

    LAB_ALLOWED_AND_REPORTED(2,"实验室项目审核通过"),

    SECONDARY_UNIT_ALLOWED(3,"二级单位审核通过,待上报"),

    SECONDARY_UNIT_ALLOWED_AND_REPORTED(4,"二级单位审核通过并已上报"),

    ESTABLISH(5,"职能部门审核通过,立项"),

    /**
     * 取消中期, 退回修改,不通过
     */
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

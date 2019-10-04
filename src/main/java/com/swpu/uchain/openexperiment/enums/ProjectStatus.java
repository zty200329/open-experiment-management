package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 项目进度状态
 */
@Getter
public enum ProjectStatus {
    /**
     * 申报
     */
    DECLARE(0),
    /**
     * 实验室审核通过
     */
    LAB_ALLOWED(1),
    /**
     * 二级单位审核通过
     */
    SECONDARY_UNIT_ALLOWED(2),
    /**
     * 立项
     */
    ESTABLISH(3),
    /**
     * 驳回修改
     */
    REJECT_MODIFY(4),
    /**
     * 已上报学院领导
     */
    REPORT_COLLEGE_LEADER(5),
    /**
     * 中期检查
     */
    MID_TERM_INSPECTION(6),
    /**
     * 结题项目
     */
    CONCLUDED(7),
    ;

    private Integer value;

    ProjectStatus(Integer value) {
        this.value = value;
    }
}

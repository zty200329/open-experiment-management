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
     * 立项
     */
    ESTABLISH(1),
    /**
     * 驳回修改
     */
    REJECT_MODIFY(2),
    /**
     * 已上报学院领导
     */
    REPORT_COLLEGE_LEADER(3),
    /**
     * 中期检查
     */
    MID_TERM_INSPECTION(4),
    /**
     * 结题项目
     */
    CONCLUDED(5),
    ;

    private Integer value;

    ProjectStatus(Integer value) {
        this.value = value;
    }
}

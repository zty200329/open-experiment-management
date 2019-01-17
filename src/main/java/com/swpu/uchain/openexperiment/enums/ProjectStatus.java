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
     * 项目开展中
     */
    PROCESSING(2),
    /**
     * 审核阶段
     */
    CHECK(3),
    /**
     * 结项失败
     */
    FAIL(4),
    /**
     * 结项通过
     */
    PASS(5),
    /**
     * 项目已完结
     */
    OVER(6),
    ;

    private Integer value;

    ProjectStatus(Integer value) {
        this.value = value;
    }
}

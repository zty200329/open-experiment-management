package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 实验类型：
 * 1.科研，2.科技活动，3.自选课题，4.计算机应用，5.人文素质
 */
@Getter
public enum ExperimentType {
    /**
     * 科研
     */
    SCIENTIFIC_RESEARCH(1),
    /**
     * 科技活动
     */
    TECHNOLOGICAL_ACTIVITY(2),
    /**
     * 自选课题
     */
    OPTIONAL_TOPIC(3),
    /**
     * 计算机应用
     */
    COMPUTER_APPLICATION(4),
    /**
     * 人文素质
     */
    HUMANISTIC_QUALITY(5),
    ;
    private Integer value;

    ExperimentType(Integer value) {
        this.value = value;
    }
}

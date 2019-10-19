package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 项目类型:
 * 1.普通，2.重点
 */
@Getter
public enum ProjectType {
    /**
     * 重点项目
     */
    KEY(2),
    /**
     * 普通项目
     */
    GENERAL(1)
    ;
    private Integer value;

    ProjectType(Integer value) {
        this.value = value;
    }
}

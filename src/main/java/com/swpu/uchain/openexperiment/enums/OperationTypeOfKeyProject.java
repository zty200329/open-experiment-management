package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 *
 * 重点项目操作类型
 * @author dengg
 */

@Getter
public enum  OperationTypeOfKeyProject {

    /**
     *
     */
    APPLY(20,"同意"),
    REJECT(21,"驳回"),
    REPORT(22,"上报");

    private Integer value;

    private String tips;

    OperationTypeOfKeyProject(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }

}

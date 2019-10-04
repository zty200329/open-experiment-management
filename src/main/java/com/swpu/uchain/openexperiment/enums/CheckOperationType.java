package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * 审核操作对应的类型
 * @author panghu
 */
@Getter
public enum CheckOperationType {
    /**
     *审核操作对应的类型  value:对应的传输值  tips 提示
     */
    PROJECT_OPERATION_TYPE1(1,"实验室审核立项操作"),
    PROJECT_OPERATION_TYPE2(2,"二级单位审核立项操作"),
    PROJECT_OPERATION_TYPE3(3,"职能中心审核立项操作"),
    ;

    private Integer value;

    private String tips;

    CheckOperationType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

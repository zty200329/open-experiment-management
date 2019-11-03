package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * 审核操作对应的类型
 * @author panghu
 */
@Getter
public enum OperationType {
    /**
     *审核操作对应的类型  value:对应的传输值  tips 提示
     */
    AGREE(1,"同意"),
    REJECT(2,"拒绝"),
    REPORT(3,"上报"),
    MODIFY(4,"修改"),
    OFFLINE_CHECK(5,"线下检查通过"),
    CONCLUSION(6,"立项")
    ;

    private Integer value;

    private String tips;

    OperationType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

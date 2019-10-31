package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * 审核操作对应的类型
 * @author panghu
 */
@Getter
public enum OperationTypeOfKetProject {
    /**
     *审核操作对应的类型  value:对应的传输值  tips 提示
     */
    AGREE(1,"同意"),
    REJECT(2,"拒绝"),
    REPORT(3,"上报"),
    MODIFY(4,"修改")
    ;

    private Integer value;

    private String tips;

    OperationTypeOfKetProject(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

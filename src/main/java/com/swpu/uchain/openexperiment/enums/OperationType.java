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
    PROJECT_OPERATION_TYPE1(1,"实验室审核立项"),
    PROJECT_OPERATION_TYPE2(2,"二级单位审核立项"),
    PROJECT_OPERATION_TYPE3(3,"职能中心审核立项"),

    PROJECT_MODIFY_TYPE1(11,"职能中心对申请表进行修改"),

    PROJECT_REPORT_TYPE1(21,"实验室项目上报"),
    PROJECT_REPORT_TYPE2(22,"二级单位项目上报")

    ;

    private Integer value;

    private String tips;

    OperationType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

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
    REJECT(2,"审核拒绝"),
    REPORT(3,"上报"),
    MODIFY(4,"修改"),
    OFFLINE_CHECK(5,"线下检查通过"),
    CONCLUSION(6,"结题审核通过"),
    OFFLINE_CHECK_REJECT(7,"线下检查不通过"),
    CONCLUSION_REJECT(8,"结题审核不通过"),
    REPORT_REJECT(0,"上报拒绝"),
    INTERIM_RETURN(9,"中期退回"),
    MIDTERM_REVIEW_PASSED(10,"中期复核通过"),
    COLLEGE_PASSED_THE_EXAMINATION(11,"学院结题审核通过"),
    FUNCTIONAL_PASSED_THE_EXAMINATION(12,"职能部门结题通过")
    ;

    private Integer value;

    private String tips;

    OperationType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

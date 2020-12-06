package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * 审核操作对应的类型
 * @author zty200329
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
    COLLEGE_RETURNS(11,"学院结题审核退回"),
    COLLEGE_REVIEW_PASSED(12,"学院复核通过"),
    COLLEGE_PASSED_THE_EXAMINATION(13,"学院结题审核通过"),
    FUNCTIONAL_RETURNS(14,"职能部门审核退回"),
    FUNCTIONAL_REVIEW_PASSED(15,"职能部门复核通过"),
    FUNCTIONAL_PASSED_THE_EXAMINATION(16,"职能部门结题审核通过"),
    FUNCTIONAL_CHANGE_TO_GENERAL(17,"职能部门将重点立项转为普通立项"),
    FUNCTIONAL_ESTABLISH_RETURN(18,"职能部门立项退回修改"),
    FUNCTIONAL_ESTABLISH_PASSED(19,"职能部门立项复核通过"),
    TURN_GENERAL(20,"重点转普通"),
    ;
    private Integer value;

    private String tips;

    OperationType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

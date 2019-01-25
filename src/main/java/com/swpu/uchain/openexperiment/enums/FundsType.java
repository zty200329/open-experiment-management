package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 1.材料费,2.资料,印刷费,3.出版费,4.教师酬金
 */
@Getter
public enum FundsType {
    /**
     * 材料费
     */
    MATERIAL(1),
    /**
     * 资料.印刷费
     */
    DATA_PRINTING(2),
    /**
     * 出版费
     */
    PUBLICATION(3),
    /**
     * 教师酬金
     */
    TEACHER_REWARD(4),
    ;

    private Integer value;

    FundsType(Integer value) {
        this.value = value;
    }
}

package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 资金申请状态:1.申请中,2.已批准,3.拒绝申请
 */
@Getter
public enum FundsStatus {
    /**
     * 申请中
     */
    APPLYING(1),
    /**
     * 已批准
     */
    AGREED(2),
    /**
     * 拒绝申请
     */
    REFUSE(3),
    ;
    private Integer value;

    FundsStatus(Integer value) {
        this.value = value;
    }
}

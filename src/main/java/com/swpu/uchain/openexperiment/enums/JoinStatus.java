package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 * 加入项目组状态
 */
@Getter
public enum JoinStatus {
    /**
     * 申请中
     */
    APPLYING(1),
    /**
     * 已加入
     */
    JOINED(2);
    private Integer value;

    JoinStatus(Integer value) {
        this.value = value;
    }
}

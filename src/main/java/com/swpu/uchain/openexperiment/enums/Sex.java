package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 性别
 */
@Getter
public enum Sex {
    /**
     * 男性
     */
    MALE("男"),
    /**
     * 女性
     */
    FEMALE("女");
    private String value;

    Sex(String value) {
        this.value = value;
    }
}

package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 用户类型:
 * 1.学生,2.教师
 */
@Getter
public enum UserType {
    /**
     * 学生
     */
    STUDENT(1),
    /**
     * 教师
     */
    TEACHER(2),
    ;

    private Integer value;

    UserType(Integer value) {
        this.value = value;
    }
}

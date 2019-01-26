package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 用户类型:
 * 1.学生,2.教师,3教授,4.副教授
 */
@Getter
public enum UserType {
    /**
     * 学生
     */
    STUDENT(1, "学生"),
    /**
     * 讲师
     */
    LECTURER(2, "讲师"),
    /**
     * 教授
     */
    PROFESS(3, "教授"),
    /**
     * 副教授
     */
    ASSOCIATE_PROFESSOR(4, "副教授"),

    ;

    private Integer value;
    private String message;

    UserType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }
}

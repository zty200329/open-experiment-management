package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 项目组成员身份
 * 1.指导教师2.项目组长3.普通成员
 */
@Getter
public enum MemberRole {

    /**
     * 指导教师
     */
    GUIDANCE_TEACHER(1),
    /**`
     * 项目组组长
     */
    PROJECT_GROUP_LEADER(2),
    /**
     * 普通成员
     */
    NORMAL_MEMBER(3)
    ;

    MemberRole(Integer value) {
        this.value = value;
    }

    private Integer value;

}

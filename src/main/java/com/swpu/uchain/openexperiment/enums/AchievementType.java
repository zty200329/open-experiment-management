package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @author zty
 * @date 2020/5/28 下午11:01
 * @description: 标志性成果类型
 */
@Getter
public enum AchievementType {
    /**
     *
     */
    PATENT(1,"发明专利"),
    UTILITY_MODEL_PATENTS(2,"实用新型专利"),
    PAPER(3,"论文"),
    CONTEST_WINNING(4,"竞赛获奖"),
    SELF_DEVELOPED_SELF_MADE_EQUIPMENT(5,"自研自制设备"),
    OTHERS(6,"其他");

    private Integer value;

    private String tips;

    AchievementType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

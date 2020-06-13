package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

@Getter
public enum KeyGrade {
    /**
     *
     */
    QUALIFIED(0,"合格"),
    FIRST_PRIZE(1,"一等奖"),
    SECOND_PRIZE(2,"二等奖"),
    THRID_PRIZE(3,"三等奖")
    ;


    private Integer value;

    private String tips;

    KeyGrade(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

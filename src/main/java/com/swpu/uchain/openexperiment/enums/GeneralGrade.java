package com.swpu.uchain.openexperiment.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author zty
 */

@Getter
public enum GeneralGrade {
    /**
     *
     */
    PASS(0,"通过"),
    GREAT(1,"优秀");
    private Integer value;

    private String tips;

    GeneralGrade(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }

    public static String getTips(Integer value) {
        GeneralGrade[] carTypeEnums = values();
        for (GeneralGrade carTypeEnum : carTypeEnums) {
            if (carTypeEnum.value().equals(value)) {
                return carTypeEnum.tips();
            }
        }
        return null;
    }

    private Integer value() {
        return this.value;
    }

    private String tips() {
        return this.tips;
    }
}

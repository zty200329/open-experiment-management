package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

@Getter
public enum FundType {

    /**
     *
     */
    EXPERIMENTAL_MATERIAL_FEE(1,"实验材料费"),
    INFORMATION_PRINTING_FEE(2,"资料印刷费"),
    PUBLISH_FEE(3,"出版费"),
    TEACHER_FEE(4,"教师酬金费"),
    OTHER_FEE(5,"其他费用");


    FundType(Integer value, String tops) {
        this.value = value;
        this.tops = tops;
    }

    /**
     * 取值
     */
    private Integer value;
    /**
     * 提示
     */
    private String tops;

}

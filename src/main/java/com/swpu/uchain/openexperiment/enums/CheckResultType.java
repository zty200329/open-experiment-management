package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @author panghu
 */

@Getter
public enum  CheckResultType {

    /**
     *
     */
    PASS("1","通过"),
    REJECTED("2","驳回");

    /**
     * 设置为String方便扩展
     */
    private String value;

    private String tips;

    CheckResultType(String value, String tips) {
        this.value = value;
        this.tips = tips;
    }

}

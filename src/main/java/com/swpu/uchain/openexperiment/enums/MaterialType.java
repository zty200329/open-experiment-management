package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

@Getter
public enum  MaterialType {

    /**
     *
     */
    APPLY_MATERIAL(1,"申请材料"),
    CONCLUSION_MATERIAL(2,"结题材料"),
    ATTACHMENT_FILE(10,"附件");

    MaterialType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }


    /**
     * 值
     */
    private Integer value;

    /**
     * 提示
     */
    private String tips;

}

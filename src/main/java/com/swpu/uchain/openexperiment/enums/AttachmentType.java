package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @author dengg
 */
@Getter
public enum AttachmentType {
    /**
     *
     */
    GENERAL_PROJECT_FINAL_ACCEPTANCE_REPORT(6,"普通项目结题验收报告"),
    KEY_PROJECT_FINAL_ACCEPTANCE_REPORT(7,"重点项目结题验收报告"),
    GENERAL_PROJECT_EXPERIMENT_REPORT(8,"普通项目实验报告")
    ;

    private Integer value;

    private String tips;

    AttachmentType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }
}

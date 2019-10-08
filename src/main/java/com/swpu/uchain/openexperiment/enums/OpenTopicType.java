package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @author panghu
 */
@Getter
public enum  OpenTopicType {
    /**
     *
     */
    OPEN_TOPIC_ALL(1,"开放选题"),
    NOT_OPEN_TOPIC(2,"不开放选题"),
    OPEN_TOPIC_PART(3,"开放部分选题");

    private Integer value;

    private String tips;

    OpenTopicType(Integer value, String tips) {
        this.value = value;
        this.tips = tips;
    }

}

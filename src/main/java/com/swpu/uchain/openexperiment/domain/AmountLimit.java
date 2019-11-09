package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

/**
 * @author dengg
 */
@Data
public class AmountLimit {

    private Long id;

    private Integer maxAmount;

    private Integer minAmount;

    /**
     * 限制学院
     */
    private Integer limitCollege;

    /**
     * 限制单位
     */
    private Integer projectType;

}

package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import javax.validation.constraints.Null;

/**
 * @author dengg
 */
@Data
public class AmountLimit {

    private Integer id;

    private Integer maxAmount;

    @Null
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

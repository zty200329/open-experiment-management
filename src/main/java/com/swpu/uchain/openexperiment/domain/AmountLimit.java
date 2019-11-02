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

    private Integer limitCollege;

    private Integer limitUnit;

}

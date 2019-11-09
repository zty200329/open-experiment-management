package com.swpu.uchain.openexperiment.VO.limit;

import lombok.Data;

/**
 * @author dengg
 */
@Data
public class AmountLimitVO {

    private Integer id;

    private Integer college;

    private Integer projectType;

    private Integer maxAmount;

    private Integer minAmount;

}

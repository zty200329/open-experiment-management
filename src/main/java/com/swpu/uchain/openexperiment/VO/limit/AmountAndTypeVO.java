package com.swpu.uchain.openexperiment.VO.limit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author dengg
 */
@Data
public class AmountAndTypeVO {

    private Integer id;

    private Integer projectType;

    private Integer maxAmount;

    @JsonIgnore
    private Integer minAmount;



}

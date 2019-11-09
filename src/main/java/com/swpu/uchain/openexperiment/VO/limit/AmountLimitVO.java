package com.swpu.uchain.openexperiment.VO.limit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author dengg
 */
@Data
public class AmountLimitVO {

    private Integer id;

    private Integer college;

    private Integer projectType;

    private Integer maxAmount;

    private Date startTime;

    private Date endTime;

    @JsonIgnore
    private Integer minAmount;

}

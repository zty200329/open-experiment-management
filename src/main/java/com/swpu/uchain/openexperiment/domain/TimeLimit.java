package com.swpu.uchain.openexperiment.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author dengg
 */
@Data
public class TimeLimit {

    /**
     * {@link com.swpu.uchain.openexperiment.enums.TimeLimitType}
     */
    @ApiModelProperty("时间限制类型")
    private Integer timeLimitType;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("截止时间")
    private Date endTime;

    @ApiModelProperty("限制学院")
    private Integer limitCollege;

}

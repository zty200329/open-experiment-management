package com.swpu.uchain.openexperiment.form.time;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author dengg
 */
@Data
public class TimeLimitForm {

    /**
     * {@link com.swpu.uchain.openexperiment.enums.TimeLimitType}
     */
    @Min(1)
    @Max(7)
    @NotNull
    @ApiModelProperty("时间限制类型")
    private Integer timeLimitType;

    @NotNull
    @ApiModelProperty("开始时间")
    private Date startTime;

    @NotNull
    @ApiModelProperty("截止时间")
    private Date endTime;

}

package com.swpu.uchain.openexperiment.form.announcement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @author dengg
 */
@Data
public class QueryCondition {

    @ApiModelProperty("标题")
    private String title;

    @Max(2)
    @Min(1)
    @ApiModelProperty("发布状态 1，已发布 2，未发布")
    private Integer status;

    @ApiModelProperty("起始时间")
    private Date startTime;

    @ApiModelProperty("终止时间")
    private Date endTime;


}

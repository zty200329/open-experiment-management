package com.swpu.uchain.openexperiment.form.amount;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class ProjectApplyAmountLimitForm {

    @ApiModelProperty("更新的时候传，插入不传")
    private Integer id;
    /**
     * {@link com.swpu.uchain.openexperiment.enums.ProjectType}
     */
    @Max(2)
    @Min(1)
    private Integer projectType;

    @Min(0)
    private Integer maxAmount;

}

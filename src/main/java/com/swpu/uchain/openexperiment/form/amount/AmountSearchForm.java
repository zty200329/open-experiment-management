package com.swpu.uchain.openexperiment.form.amount;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AmountSearchForm {

    @ApiModelProperty("学院")
    private Integer college;

    @ApiModelProperty("2.重点 1,普通")
    private Integer projectType;

}

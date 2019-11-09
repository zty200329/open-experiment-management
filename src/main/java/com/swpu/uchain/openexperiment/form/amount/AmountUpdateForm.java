package com.swpu.uchain.openexperiment.form.amount;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author dengg
 */
@Data
public class AmountUpdateForm {

    @NotNull
    private Integer maxAmount;

    @NotNull
    private Integer minAmount;

    @NotNull
    @ApiModelProperty("id,查询中返回的ID")
    private Integer id;

}

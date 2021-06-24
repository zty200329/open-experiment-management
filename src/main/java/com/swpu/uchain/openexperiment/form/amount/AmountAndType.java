package com.swpu.uchain.openexperiment.form.amount;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author dengg
 */
@Data
public class AmountAndType {

    private Integer id;

    @Max(3)
    @Min(1)
    private Integer projectType;

    @NotNull
    private Integer maxAmount;

}

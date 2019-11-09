package com.swpu.uchain.openexperiment.form.amount;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: panghu
 * @Date:
 */
@Data
public class AmountLimitForm {

    @NotNull
    private Integer maxAmount;

    @NotNull
    private Integer minAmount;

    @NotNull
    private Integer limitCollege;

    @NotNull
    private Integer projectType;
}

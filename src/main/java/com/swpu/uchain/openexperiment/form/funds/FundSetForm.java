package com.swpu.uchain.openexperiment.form.funds;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author dengg
 */
@Data
public class FundSetForm {

    @NotNull
    private Long projectId;

    @Min(0)
    private Float supportAmount;

}

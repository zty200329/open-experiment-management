package com.swpu.uchain.openexperiment.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author panghu
 */
@Data
public class ConfirmForm {

    @NotNull
    @ApiModelProperty("项目ID")
    private Long projectId;

    @Max(2)
    @Min(1)
    @ApiModelProperty("1.确认  2.拒绝")
    private Integer result;

}

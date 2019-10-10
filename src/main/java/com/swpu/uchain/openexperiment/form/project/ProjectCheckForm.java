package com.swpu.uchain.openexperiment.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 项目审核表单
 *
 * @author panghu
 */
@Data
public class ProjectCheckForm {

    @NotNull
    @ApiModelProperty("项目的主键")
    private Long projectId;

    @NotNull
    @ApiModelProperty("拒绝或者同意的的原因")
    private String reason;

}

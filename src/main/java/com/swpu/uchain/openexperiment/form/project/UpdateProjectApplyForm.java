package com.swpu.uchain.openexperiment.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-3-20
 * @Description:
 */
@Data
public class UpdateProjectApplyForm extends CreateProjectApplyForm{
    @ApiModelProperty("项目组id")
    @NotNull(message = "项目组id不能为空")
    private Long projectGroupId;

}

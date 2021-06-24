package com.swpu.uchain.openexperiment.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zty200329
 * @version 1.0
 * @date 2020/11/3 8:40 下午
 */
@Data
public class ChangeKeyProjectToGeneralForm {

    @NotNull
    @ApiModelProperty("项目的主键")
    private Long projectId;

    @NotNull
    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("所需经费支持")
    private Float applyFunds;

}

package com.swpu.uchain.openexperiment.form.check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Null;

/**
 * @author dengg
 */
@Data
public class KeyProjectCheck {

    @ApiModelProperty("项目编号")
    private Long projectId;

    @ApiModelProperty("操作理由")
    private String reason;

}

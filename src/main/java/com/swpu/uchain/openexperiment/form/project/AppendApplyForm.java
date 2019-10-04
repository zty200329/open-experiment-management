package com.swpu.uchain.openexperiment.form.project;

import com.swpu.uchain.openexperiment.form.funds.FundsForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-26
 * @Description:
 * 项目组组长填报重点项目立项附加信息表
 */
@Data
public class AppendApplyForm {

    @ApiModelProperty("项目组id")
    @NotNull(message = "项目组id不能为空")
    private Long projectGroupId;

    @NotNull(message = "资金申请表不能为空")
    @ApiModelProperty("资金申请数组")
    private FundsForm[] fundsForms;
}

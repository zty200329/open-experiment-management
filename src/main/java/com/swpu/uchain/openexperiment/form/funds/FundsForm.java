package com.swpu.uchain.openexperiment.form.funds;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author dengg
 */
@Data
public class FundsForm {

    @ApiModelProperty("项目编号")
    private Long projectId;

    @ApiModelProperty("资金详情")
    List<FundForm> fundFormList;
}

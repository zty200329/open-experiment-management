package com.swpu.uchain.openexperiment.form.funds;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-26
 * @Description:
 */
@Data
public class FundsForm {

    @ApiModelProperty("资金的id,不为空时表示更新,为空表示添加")
    private Long fundsId;

    @ApiModelProperty("资金类型: 1.材料费,2.资料,印刷费,3.出版费,4.教师酬金,5.其他合理费用")
    @NotNull(message = "资金类型不能为空")
    private Integer type;

    @ApiModelProperty("当资金类型为(5.其他合理费用)时需要进行描述具体用途")
    private String use;

    @ApiModelProperty("具体金额")
    @NotNull(message = "具体金额不能为空")
    private Integer amount;
}

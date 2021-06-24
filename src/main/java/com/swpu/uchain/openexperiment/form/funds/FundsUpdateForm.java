package com.swpu.uchain.openexperiment.form.funds;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量修改项目金额所需的金额
 *
 * @author dengg
 */
@Data
public class FundsUpdateForm {

    @NotNull(message = "编号长度不为空")
    @ApiModelProperty("项目编号")
    private List<Long> projectIdList;

    @NotNull(message = "金额不能为空")
    @ApiModelProperty("修改后的金额")
    private Float fundsMount;

}

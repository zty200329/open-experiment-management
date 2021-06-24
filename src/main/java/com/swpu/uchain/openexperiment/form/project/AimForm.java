package com.swpu.uchain.openexperiment.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 指定用户身份表单
 */
@Data
public class AimForm {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotNull(message = "项目组id不能为空")
    private Long projectGroupId;

    @NotNull(message = "指定的成员角色不能为空")
    @Min(2)
    @Max(3)
    @ApiModelProperty("成员角色:1.指导教师2.项目组长3.普通成员")
    private Integer memberRole;
}

package com.swpu.uchain.openexperiment.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 登录表单
 */
@Data
public class LoginForm {
    @NotNull(message = "学号或工号不能为空")
    private String userCode;
    @ApiModelProperty("用户角色选择")
    @NotNull
    private Integer role;
}

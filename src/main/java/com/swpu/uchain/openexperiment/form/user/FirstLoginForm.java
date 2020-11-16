package com.swpu.uchain.openexperiment.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zty200329
 * @version 1.0
 * @date 2020/11/16 9:22 上午
 */
@Data
public class FirstLoginForm {
    @NotNull(message = "学号或工号不能为空")
    private String userCode;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "验证码不能为空")
    private String verifyCode;
}

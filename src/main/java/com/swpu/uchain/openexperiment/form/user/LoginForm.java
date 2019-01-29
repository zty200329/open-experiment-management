package com.swpu.uchain.openexperiment.form.user;

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
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "验证码不能为空")
    private String verifyCode;
}

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
public class LoginChangeForm {

    @ApiModelProperty("用户角色选择")
    @NotNull
    private Integer role;
}

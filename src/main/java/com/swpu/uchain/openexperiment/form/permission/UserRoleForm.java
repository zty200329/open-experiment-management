package com.swpu.uchain.openexperiment.form.permission;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 */
@Data
public class UserRoleForm {
    @NotNull(message = "用户id不能为空")
    private Long userId;
    @NotNull(message = "用户id不能为空")
    private Long roleId;
}

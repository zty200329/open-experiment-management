package com.swpu.uchain.openexperiment.form.permission;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 */
@Data
public class RoleForm {
    @NotNull(message = "角色id不能为空")
    private Long roleId;
    @NotNull(message = "角色名不能为空")
    @Length(max = 20, message = "角色名长度不能超过20")
    private String roleName;
}

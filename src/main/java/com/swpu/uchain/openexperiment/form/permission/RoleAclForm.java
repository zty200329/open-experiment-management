package com.swpu.uchain.openexperiment.form.permission;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 */
@Data
public class RoleAclForm {
    @NotNull(message = "角色id不能为空")
    private Long roleId;
    @NotNull(message = "权限id不能为空")
    private Long aclId;
}

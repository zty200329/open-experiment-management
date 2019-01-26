package com.swpu.uchain.openexperiment.form.project;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 */
@Data
public class JoinForm {
    @NotNull(message = "用户id不能为空")
    private Long userId;
    @NotNull(message = "项目组id不能为空")
    private Long projectGroupId;
}

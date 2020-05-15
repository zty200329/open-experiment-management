package com.swpu.uchain.openexperiment.form.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zty
 * @date 2020/5/15 下午12:15
 * @description:
 */
@Data
public class GetAllPermissions {

    @NotNull(message = "请输入学号")
    private String userCode;
}

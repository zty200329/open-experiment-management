package com.swpu.uchain.openexperiment.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @Author: clf
 * @Date: 19-1-29
 * @Description:
 * 用户信息更新表单
 */
@Data
public class UserUpdateForm {

    @NotNull(message = "用户id为空")
    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("密码")
    private String password;

    @NotNull(message = "邮箱号不能为空")
    @ApiModelProperty("邮箱号")
    private String email;

    @ApiModelProperty("固定电话")
    private String fixPhone;

    @NotNull(message = "身份证号不能为空")
    @ApiModelProperty("身份证号")
    private String idCard;

    @NotNull(message = "手机号不能为空")
    @ApiModelProperty("手机号")
    private String mobilePhone;

    @NotNull(message = "qq号不能为空")
    @ApiModelProperty("qq号")
    private String qqNum;

    @NotNull(message = "真实姓名不能为空")
    @ApiModelProperty("真实姓名")
    private String realName;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty("性别: 男 || 女")
    @Length(max = 2, message = "字符过长不合法")
    private String sex;
}

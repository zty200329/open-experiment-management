package com.swpu.uchain.openexperiment.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-21
 * @Description:
 * 学生参与项目申请表
 */
@Data
public class JoinProjectApplyForm {
    @ApiModelProperty("选择申请参与项目组id")
    @NotNull(message = "项目组id不能为空")
    private Long projectGroupId;
    @NotNull(message = "技术角色不能为空")
    @Length(max = 50, message = "字数不能超过50字")
    private String technicalRole;
    @ApiModelProperty("自我评价")
    private String personalJudge;
}

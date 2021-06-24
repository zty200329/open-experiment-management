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

    @ApiModelProperty("担任角色")
    private String technicalRole;

    @ApiModelProperty("自我评价")
    private String personalJudge;
}

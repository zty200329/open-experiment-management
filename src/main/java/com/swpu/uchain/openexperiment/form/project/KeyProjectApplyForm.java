package com.swpu.uchain.openexperiment.form.project;

import com.swpu.uchain.openexperiment.form.funds.FundsForm;
import com.swpu.uchain.openexperiment.form.user.StuMember;
import com.swpu.uchain.openexperiment.form.user.TeacherMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author dengg
 */
@Data
public class KeyProjectApplyForm {

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("项目类型")
    private Integer projectType;

    @ApiModelProperty("项目成员")
    private List<StuMember> members;

    @ApiModelProperty("申请费用")
    private float funds;

    @ApiModelProperty("资金说明")
    private List<FundsForm> fundsList;

    @ApiModelProperty("教师信息")
    private List<TeacherMember> teachers;

}

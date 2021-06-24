package com.swpu.uchain.openexperiment.form.project;

import com.swpu.uchain.openexperiment.form.user.StuMember;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author dengg
 */
@Data
public class KeyProjectApplyForm {

    @ApiModelProperty("项目ID")
    private Long projectId;

    @ApiModelProperty("项目成员")
    private List<StuMember> members;



}

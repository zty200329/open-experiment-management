package com.swpu.uchain.openexperiment.form.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectMenmerApplyForm {
    
    @ApiModelProperty("项目ID")
    private Long projectId;
    
    @ApiModelProperty("申请理由")
    private String reason;
    
}

package com.swpu.uchain.openexperiment.form.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author dengg
 */
@Data
public class MemberQueryCondition {

    @ApiModelProperty("项目主键--id")
    private Long id;

    /**
     * {@link com.swpu.uchain.openexperiment.enums.JoinStatus}
     */
    @Min(1)
    @Max(3)
    @ApiModelProperty("成员加入状态")
    private Integer status;

}

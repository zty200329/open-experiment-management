package com.swpu.uchain.openexperiment.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zty
 * @date 2020/6/1 下午8:55
 * @description: 标志性结果表单
 */
@Data
public class IconicResultForm {

    @NotNull(message = "项目id不能为空")
    private Long projectId;

    @NotNull(message = "成果类型不能为空")
    @ApiModelProperty("成果类型")
    private Integer value;

    @NotNull(message = "成果出处不能为空")
    private String provenance;

    @NotNull(message = "成果发表或授权时间不能为空")
    private Date issuingTime;

    @NotNull(message = "成果名称不能为空")
    private String issuingName;
}

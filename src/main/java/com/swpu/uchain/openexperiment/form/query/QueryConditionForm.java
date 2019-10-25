package com.swpu.uchain.openexperiment.form.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @author dengg
 */
@Data
public class QueryConditionForm {

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("学院")
    private Integer college;

    @ApiModelProperty("建议分组")
    private Integer suggestGroupType;

    @ApiModelProperty("实验类型: 1.科研,2.科技活动,3.自选课题,4.计算机应用,5.人文素质")
    private Integer experimentType;

    @ApiModelProperty("实验开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("实验结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("限选专业--选填")
    private String limitMajor;

    @ApiModelProperty("限选学院--选填")
    private String limitCollege;

    @ApiModelProperty("限选年级")
    private String limitGrade;

    @Null
    @ApiModelProperty("项目状态--不填")
    private Integer status;
}

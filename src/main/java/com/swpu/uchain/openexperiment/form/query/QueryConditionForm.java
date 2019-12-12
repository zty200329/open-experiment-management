package com.swpu.uchain.openexperiment.form.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @author dengg
 */
@Data
public class QueryConditionForm {

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("指导教师")
    private String guideTeacher;

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

    @ApiModelProperty("项目状态  0-7")
    private Integer status;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("所属学院")
    private Integer subordinateCollege;

    @ApiModelProperty("申请经费")
    private Float applyFunds;

    /**
     * {@link com.swpu.uchain.openexperiment.enums.ProjectType}
     */
    @ApiModelProperty("项目类型")
    private Integer projectType;
}

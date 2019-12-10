package com.swpu.uchain.openexperiment.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-3-20
 * @Description:
 */
@Data
public class UpdateProjectApplyForm{

    @ApiModelProperty("项目组id")
    @NotNull(message = "项目组id不能为空")
    private Long projectGroupId;

    @ApiModelProperty("实验室名称")
    @NotNull(message = "实验室名称必填")
    private String labName;

    @ApiModelProperty("实验地点")
    @NotNull(message = "实验地点称必填")
    private String address;

    @ApiModelProperty("实验条件  （指设备、仪器、材料、场地、教学资料、管理措施、人员配备等）")
    @NotNull(message = "实验设备条件必填")
    private String experimentCondition;

    @ApiModelProperty("项目名称")
    @NotNull(message = "项目名称必填")
    private String projectName;

    @ApiModelProperty("实验类型: 1.科研,2.科技活动,3.自选课题,4.计算机应用,5.人文素质")
    @NotNull(message = "实验类型必选")
    private Integer experimentType;

    @ApiModelProperty("项目类型: 1.普通,2.重点")
    @NotNull(message = "项目类型必选")
    private Integer projectType;

    @ApiModelProperty("1.A组石工地堪,2.B组化工材料3.C组机械力学4.D电气及制作,5.E组软件与数学,6.F组经管法律艺体人文")
    @NotNull(message = "建议评审分组必选")
    private Character suggestGroupType;

    @ApiModelProperty("限选年级")
    private String limitGrade;

    @ApiModelProperty("限选专业")
    private String limitMajor;

    @ApiModelProperty("限选学院")
    private String limitCollege;

    @ApiModelProperty("适宜学生数")
    private Integer fitPeopleNum;

    @ApiModelProperty("成果考核方式")
    private String achievementCheck;

    @ApiModelProperty("计划实验小时数")
    @NotNull(message = "计划实验小时数不能为空")
    private Integer totalHours;

    @ApiModelProperty("实验开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("实验结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("所需经费支持")
    private Float applyFunds;

    @Null
    @ApiModelProperty("指导老师编号--选填")
    private Integer[] teacherCodes;

    @Null
    @ApiModelProperty("学生编号--选填")
    private Integer[] stuCodes;


}

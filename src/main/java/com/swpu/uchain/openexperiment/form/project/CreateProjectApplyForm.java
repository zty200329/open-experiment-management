package com.swpu.uchain.openexperiment.form.project;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-1-21
 * @Description:
 * 立项申请
 */
@Data
public class CreateProjectApplyForm {

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
    private Integer suggestGroupType;

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

    @NotNull(message = "选题不为空")
    @ApiModelProperty("是否开放选题  1.是,2否,3,开放部分")
    private Integer isOpenTopic;

    @ApiModelProperty("指导老师编号--选填")
    private String[] teacherCodes;

    @ApiModelProperty("学生编号--选填,如果开放选题选择否则不能添加学生")
    private String[] stuCodes;

    @ApiModelProperty("限选专业--选填")
    private String limitMajor;

    @ApiModelProperty("限选学院--选填")
    private String limitCollege;

    @NotNull
    @ApiModelProperty("限选年级")
    private String limitGrade;

    @ApiModelProperty("适宜学生数--选填")
    private Integer fitPeopleNum;

    @NotNull(message = "主要内容不为空")
    @ApiModelProperty("主要内容")
    private String mainContent;

}

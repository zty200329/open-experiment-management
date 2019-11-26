package com.swpu.uchain.openexperiment.VO.project;

import com.swpu.uchain.openexperiment.VO.user.Instructor;
import com.swpu.uchain.openexperiment.enums.CollegeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

/**
 * @author panghu
 */
@Data
public class OpenTopicInfo {

    @ApiModelProperty("主键")
    private Long id;

    /**
     * 所属学院  {@link CollegeType#getValue()}
     */
    private Integer subordinateCollege;

    @ApiModelProperty("项目编号")
    private String serialNumber;

    @ApiModelProperty("主要内容")
    private String mainContent;

    @ApiModelProperty("实验室名称")
    private String labName;

    @ApiModelProperty("实验地点")
    private String address;

    @ApiModelProperty("实验条件  （指设备、仪器、材料、场地、教学资料、管理措施、人员配备等）")
    private String experimentCondition;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("实验类型: 1.科研,2.科技活动,3.自选课题,4.计算机应用,5.人文素质")
    private Integer experimentType;

    @ApiModelProperty("项目类型: 1.普通,2.重点")
    private Integer projectType;

    @ApiModelProperty("1.A组石工地堪,2.B组化工材料3.C组机械力学4.D电气及制作,5.E组软件与数学,6.F组经管法律艺体人文")
    private Integer suggestGroupType;

    @ApiModelProperty("限选年级")
    private String limitGrade;

    @ApiModelProperty("成果考核方式")
    private String achievementCheck;

    @ApiModelProperty("计划实验小时数")
    private Integer totalHours;

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

    @ApiModelProperty("适宜学生数--选填")
    private Integer fitPeopleNum;

    /**
     * 已选择该项目的学生
     */
    private Integer amountOfSelected;

    /**
     * 指导老师信息
     */
    private List<Instructor> teachers;

}

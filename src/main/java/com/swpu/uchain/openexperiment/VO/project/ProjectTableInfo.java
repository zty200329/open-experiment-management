package com.swpu.uchain.openexperiment.VO.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 总览表的项目信息
 * @author panghu
 */
@Data
public class ProjectTableInfo {

    /**
     * 所属学院/中心
     */
    private Integer college;

    /**
     * 项目编号
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 实验类型
     */
    private Integer experimentType;

    /**
     * 实验计划时长
     */
    private Integer totalHours;

    /**
     * 指导教师
     */
    private String leadTeacher;

    /**
     * 负责学生
     */
    private String leadStudent;

    /**
     * 专业年级
     */
    private String gradeAndMajor;

    /**
     * 实验开始时间
     */
    @JsonFormat(timezone = "GMT+8")
    private String startTime;

    /**
     * 实验截止时间
     */
    @JsonFormat(timezone = "GMT+8")
    private String endTime;

    /**
     * 所属实验室
     */
    private String labName;

    /**
     * 实验室地点
     */
    private String address;

    /**
     * 负责学生电话
     */
    private String leadStudentPhone;

    /**
     * 申请金额
     */
    private Float applyFunds;

    /**
     * 1组石工地堪,
     * 2.组化工材料
     * 3.组机械力学
     * 4.电气及制作,
     * 5.组软件与数学
     * 6.组经管法律艺体人文
     */
    private Integer suggestGroupType;


}

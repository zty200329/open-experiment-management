package com.swpu.uchain.openexperiment.VO.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 * 我的项目管理
 */
@Data
public class MyProjectVO implements Serializable {
    /**
     * 项目id
     */
    private Long projectGroupId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目类型
     */
    private Integer projectType;
    /**
     * 实验总时长
     */
    private Integer totalHours;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 截止时间
     */
    private Date endTime;
    /**
     * 立项时间
     */
    private Date createTime;
    /**
     * 当前状态
     */
    private Integer status;
    /**
     * 项目详情
     */
    private ProjectDetails projectDetails;
    /**
     * 实验条件
     */
    private String experimentCondition;
    /**
     * 建议分组类型
     */
    private Integer suggestGroupType;
    /**
     * 实验类型
     */
    private Integer experimentType;
    /**
     * 限制学院
     */
    private String limitCollege;
    /**
     * 限制专业
     */
    private String limitMajor;
    /**
     * 限制年级
     */
    private Integer limitGrade;
    /**
     * 适合人数
     */
    private Integer fitPeopleNum;
    /**
     * 中期检查
     */
    private String achievementCheck;
}

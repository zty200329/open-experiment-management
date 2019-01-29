package com.swpu.uchain.openexperiment.VO.project;

import lombok.Data;

import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 * 我的项目管理
 */
@Data
public class MyProjectVO {
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
}

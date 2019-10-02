package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author panghu
 */
@Data
public class ProjectGroup implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 实验地点
     */
    private String address;

    /**
     * 小组申报时间
     */
    private Date createTime;

    /**
     * 申报者ID
     */
    private Long creatorId;

    /**
     * 实验截止时间
     */
    private Date endTime;

    /**
     * 实验条件--描述
     */
    private String experimentCondition;

    /**
     * 建议分组
     * 1.A组石工地堪,2.B组化工材料3.C组机械力学4.D电气及制作,5.E组软件与数学,6.F组经管法律艺体人文
     */
    private Character suggestGroupType;

    /**项目类型: 1.普通,2.重点
     *
     */
    private Integer experimentType;

    /**
     * 成果考核方式
     */
    private String achievementCheck;

    /**
     * 限选学院
     */
    private String limitCollege;

    /**
     * 限选专业
     */
    private String limitMajor;

    /**
     * 限选年级
     */
    private Integer limitGrade;

    /**
     *  适宜人数  (项目建议人数)
     */
    private Integer fitPeopleNum;

    /**
     * 总计划实验小时
     */
    private Integer totalHours;

    /**
     * 实验室名称
     */
    private String labName;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目类型  1.普通,2.重点
     */
    private Integer projectType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 项目开展进度
     */
    private Integer status;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;


    public ProjectGroup() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", address=").append(address);
        sb.append(", createTime=").append(createTime);
        sb.append(", creatorId=").append(creatorId);
        sb.append(", endTime=").append(endTime);
        sb.append(", experimentCondition=").append(experimentCondition);
        sb.append(", suggestGroupType=").append(suggestGroupType);
        sb.append(", experimentType=").append(experimentType);
        sb.append(", achievementCheck=").append(achievementCheck);
        sb.append(", limitCollege=").append(limitCollege);
        sb.append(", limitMajor=").append(limitMajor);
        sb.append(", limitGrade=").append(limitGrade);
        sb.append(", fitPeopleNum=").append(fitPeopleNum);
        sb.append(", totalHours=").append(totalHours);
        sb.append(", labName=").append(labName);
        sb.append(", projectName=").append(projectName);
        sb.append(", projectType=").append(projectType);
        sb.append(", startTime=").append(startTime);
        sb.append(", status=").append(status);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
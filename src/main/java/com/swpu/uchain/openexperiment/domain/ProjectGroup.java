package com.swpu.uchain.openexperiment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Null;
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
    private Integer suggestGroupType;

    /**项目类型: 1.普通,2.重点
     *
     */
    private Integer experimentType;

    /**
     * 成果考核方式
     */
    private String achievementCheck;

    /**
     * 是否开放选题
     */
    private Integer isOpenTopic;

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
    private String limitGrade;

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

    /**
     * 所需经费支持
     */
    private Float applyFunds;

    /**
     * 主要内容
     */
    private String mainContent;

    /**
     * 所属学院
     */
    @JsonIgnore
    private Integer subordinateCollege;


    /**
     * 已选项目人数
     */
    @JsonIgnore
    @Null
    private Integer numberOfTheSelected;

    private static final long serialVersionUID = 1L;


    public ProjectGroup() {
    }


}
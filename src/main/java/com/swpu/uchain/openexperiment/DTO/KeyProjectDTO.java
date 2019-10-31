package com.swpu.uchain.openexperiment.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.enums.CollegeType;
import lombok.Data;

import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

/**
 * @author dengg
 */
@Data
public class KeyProjectDTO {

    /**
     * 项目编号
     */
    private Long id;


    /**
     * 重点项目创建时间
     */
    private Date createTime;

    /**
     * 创建者的学号
     */
    private Long userId;

    /**
     * 创建者名字
     */
    private String creatorName;

    /**
     * 所需经费支持
     */
    private Float applyFunds;

    /**
     * 主要内容
     */
    private String mainContent;

    /**
     * 实验地点
     */
    private String address;

    /**
     * 实验室名称
     */
    private String labName;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 重点项目审核进度
     */
    private Integer status;


    /**
     * 项目文件编号
     */
    private Long projectFileId;

    /**
     * 项目编号
     */
    private String serialNumber;

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

    /**实验类型 {@link com.swpu.uchain.openexperiment.enums.ExperimentType}
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
     * 所属学院  {@link CollegeType#getValue()}
     */
    @JsonIgnore
    private Integer subordinateCollege;

    /**
     * 已选项目人数
     */
    @Null
    private Integer numberOfTheSelected;

    /**
     * 开始时间
     */
    private Date startTime;


    /**
     * 成员信息
     */
    private List<UserMemberVO> memberVOList;



}

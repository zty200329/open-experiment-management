package com.swpu.uchain.openexperiment.DTO;

import com.swpu.uchain.openexperiment.enums.CollegeType;
import lombok.Data;

import java.util.Date;

/**
 * @author dengg
 */
@Data
public class ConclusionDTO {

    private Long id;


    /**
     * 所属学院  {@link CollegeType#getValue()}
     */
    private Integer college;

    private String labName;

    private String projectName;

    /**实验类型 {@link com.swpu.uchain.openexperiment.enums.ExperimentType}
     *
     */
    private Integer experimentType;

    /**
     * 总计划实验小时
     */
    private Integer totalHours;

    /**
     *  指导教师名字
     */
    private String guideTeacherName;

    /**
     * 指导教师工号
     */
    private Long guideTeacherId;


    /**
     * 学生姓名
     */
    private String userName;
    /**
     * 学生工号
     */
    private Long userId;

    /**
     * 成员职责 {@link com.swpu.uchain.openexperiment.enums.MemberRole}
     */
    private Integer userRole;

    /**
     * 专业年级
     */
    private String majorAndGrade;

    /**
     * 起止时间
     */
    private String startTimeAndEndTime;

    /**
     * 验收时间
     */
    private Date checkTime;

    /**
     * 验收结果
     */
    private String checkResult;

}

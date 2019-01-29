package com.swpu.uchain.openexperiment.VO.project;

import com.swpu.uchain.openexperiment.VO.user.UserDetailVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 普通项目立项表信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyGeneralFormInfoVO {
    /**
     * 项目组id
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
     * 实验条件
     */
    private String experimentCondition;
    /**
     * 建议评审分组
     */
    private Integer suggestGroupType;
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
     * 适宜人数
     */
    private Integer fitPeopleNum;
    /**
     * 实验总时长
     */
    private Integer totalHours;
    /**
     * 实验室名称
     */
    private String labName;
    /**
     * 项目文件id
     */
    private Long projectFileId;
    /**
     * 指导教师
     */
    private List<UserDetailVO> guideTeachers;
    /**
     * 学生成员
     */
    private List<UserDetailVO> stuMembers;

}

package com.swpu.uchain.openexperiment.VO.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swpu.uchain.openexperiment.VO.user.UserDetailVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-3-18
 * @Description:
 * 审批学生申请参与项目未审核列表
 */
@Data
public class JoinUnCheckVO implements Serializable {
    /**
     * 用户参与项目关系id
     */
    @JsonIgnore
    private Long userProjectId;
    /**
     * 项目组主键
     */
    private Long id;

    /**
     * 项目编号
     */
    private String serialNumber;
    /**
     * 用户申请参与的项目的名称
     */
    private String projectName;
    /**
     * 个人评价
     */
    private String personJudge;
    /**
     * 技术职称
     */
    private String technicalRole;
    /**
     * 用户详情信息
     */

    private Date applyTime;

    /**
     * 项目类型  1.普通,2.重点  {@link com.swpu.uchain.openexperiment.enums.ProjectType}
     */
    private Integer projectType;

    /**实验类型 {@link com.swpu.uchain.openexperiment.enums.ExperimentType}
     *
     */
    private Integer experimentType;

    /**
     * 用户加入状态
     */
    private Integer status;

    /**
     * 成员角色
     */
    private Integer memberRole;

    private UserDetailVO userDetailVO;
}

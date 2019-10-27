package com.swpu.uchain.openexperiment.VO.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swpu.uchain.openexperiment.VO.user.UserDetailVO;
import lombok.Data;

import java.io.Serializable;

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
     * 项目组id
     */
    private Long projectGroupId;
    /**
     * 用户详情信息
     */
    private UserDetailVO userDetailVO;
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
}

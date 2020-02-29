package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;

/**
 * @author zty
 */
public class Certificate implements Serializable {
    private Integer id;

    /**
     * 项目编号
     */
    private String serialNumber;

    /**
     * 项目类型：1.普通 2.重点
     */
    private Integer projectType;

    /**
     * 学号
     */
    private Long userId;

    /**
     * 成员身份：1.指导教师2.项目组长3.普通成员
     */
    private Short memberRole;

    /**
     * 实验类型：1.科研，2.科技活动，3.自选课题，4.计算机应用，5.人文素质
     */
    private Integer experimentType;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目所属学院
     */
    private String subordinateCollege;

    /**
     * 是否需要证书 0 不需要 1 需要
     */
    private Boolean isNeed;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Short getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(Short memberRole) {
        this.memberRole = memberRole;
    }

    public Integer getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(Integer experimentType) {
        this.experimentType = experimentType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getSubordinateCollege() {
        return subordinateCollege;
    }

    public void setSubordinateCollege(String subordinateCollege) {
        this.subordinateCollege = subordinateCollege == null ? null : subordinateCollege.trim();
    }

    public Boolean getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(Boolean isNeed) {
        this.isNeed = isNeed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", serialNumber=").append(serialNumber);
        sb.append(", projectType=").append(projectType);
        sb.append(", userId=").append(userId);
        sb.append(", memberRole=").append(memberRole);
        sb.append(", experimentType=").append(experimentType);
        sb.append(", projectName=").append(projectName);
        sb.append(", subordinateCollege=").append(subordinateCollege);
        sb.append(", isNeed=").append(isNeed);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
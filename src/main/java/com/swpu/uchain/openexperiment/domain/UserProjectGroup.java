package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;
import java.util.Date;

public class UserProjectGroup implements Serializable {
    private Long id;

    private Long projectGroupId;

    private Long userId;

    private String technicalRole;

    private String workDivision;

    private Integer status;

    private Integer memberRole;

    private String personalJudge;

    private Date updateTime;

    private Date joinTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectGroupId() {
        return projectGroupId;
    }

    public void setProjectGroupId(Long projectGroupId) {
        this.projectGroupId = projectGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTechnicalRole() {
        return technicalRole;
    }

    public void setTechnicalRole(String technicalRole) {
        this.technicalRole = technicalRole == null ? null : technicalRole.trim();
    }

    public String getWorkDivision() {
        return workDivision;
    }

    public void setWorkDivision(String workDivision) {
        this.workDivision = workDivision == null ? null : workDivision.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(Integer memberRole) {
        this.memberRole = memberRole;
    }

    public String getPersonalJudge() {
        return personalJudge;
    }

    public void setPersonalJudge(String personalJudge) {
        this.personalJudge = personalJudge == null ? null : personalJudge.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", projectGroupId=").append(projectGroupId);
        sb.append(", userId=").append(userId);
        sb.append(", technicalRole=").append(technicalRole);
        sb.append(", workDivision=").append(workDivision);
        sb.append(", status=").append(status);
        sb.append(", memberRole=").append(memberRole);
        sb.append(", personalJudge=").append(personalJudge);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", joinTime=").append(joinTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
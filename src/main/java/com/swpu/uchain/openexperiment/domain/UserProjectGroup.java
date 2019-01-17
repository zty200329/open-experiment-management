package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;
import java.util.Date;

public class UserProjectGroup implements Serializable {
    private Long id;

    private Long projectGroupId;

    private Long userId;

    private String technicalRole;

    private Byte memberRole;

    private String personalSkills;

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

    public Byte getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(Byte memberRole) {
        this.memberRole = memberRole;
    }

    public String getPersonalSkills() {
        return personalSkills;
    }

    public void setPersonalSkills(String personalSkills) {
        this.personalSkills = personalSkills == null ? null : personalSkills.trim();
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
        sb.append(", memberRole=").append(memberRole);
        sb.append(", personalSkills=").append(personalSkills);
        sb.append(", joinTime=").append(joinTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
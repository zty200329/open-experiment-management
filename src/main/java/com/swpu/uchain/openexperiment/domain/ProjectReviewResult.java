package com.swpu.uchain.openexperiment.domain;

import org.springframework.stereotype.Repository;

import java.io.Serializable;

public class ProjectReviewResult implements Serializable {
    private Integer id;

    private Long projectId;

    private Integer score;

    private Boolean isSupport;

    private Long operateUser;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getIsSupport() {
        return isSupport;
    }

    public void setIsSupport(Boolean isSupport) {
        this.isSupport = isSupport;
    }

    public Long getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(Long operateUser) {
        this.operateUser = operateUser;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", projectId=").append(projectId);
        sb.append(", score=").append(score);
        sb.append(", isSupport=").append(isSupport);
        sb.append(", operateUser=").append(operateUser);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
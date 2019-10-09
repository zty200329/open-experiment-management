package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;

public class Score implements Serializable {
    private Long id;

    private Long userId;

    private Integer semester;

    private Float averageScore;

    private Float achievementPoint;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Float averageScore) {
        this.averageScore = averageScore;
    }

    public Float getAchievementPoint() {
        return achievementPoint;
    }

    public void setAchievementPoint(Float achievementPoint) {
        this.achievementPoint = achievementPoint;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", semester=").append(semester);
        sb.append(", averageScore=").append(averageScore);
        sb.append(", achievementPoint=").append(achievementPoint);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
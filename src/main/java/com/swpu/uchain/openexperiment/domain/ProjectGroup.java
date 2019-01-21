package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;
import java.util.Date;

public class ProjectGroup implements Serializable {
    private Long id;

    private String address;

    private Date createTime;

    private Long creatorId;

    private Date endTime;

    private String experimentCondition;

    private Integer suggestGroupType;

    private String examination;

    private Integer experimentType;

    private String achievementCheck;

    private String limitCollege;

    private String limitMajor;

    private Integer limitGrade;

    private Integer fitPeopleNum;

    private Integer totalHours;

    private String labName;

    private String projectName;

    private Integer projectType;

    private Date startTime;

    private Integer status;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getExperimentCondition() {
        return experimentCondition;
    }

    public void setExperimentCondition(String experimentCondition) {
        this.experimentCondition = experimentCondition == null ? null : experimentCondition.trim();
    }

    public Integer getSuggestGroupType() {
        return suggestGroupType;
    }

    public void setSuggestGroupType(Integer suggestGroupType) {
        this.suggestGroupType = suggestGroupType;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination == null ? null : examination.trim();
    }

    public Integer getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(Integer experimentType) {
        this.experimentType = experimentType;
    }

    public String getAchievementCheck() {
        return achievementCheck;
    }

    public void setAchievementCheck(String achievementCheck) {
        this.achievementCheck = achievementCheck == null ? null : achievementCheck.trim();
    }

    public String getLimitCollege() {
        return limitCollege;
    }

    public void setLimitCollege(String limitCollege) {
        this.limitCollege = limitCollege == null ? null : limitCollege.trim();
    }

    public String getLimitMajor() {
        return limitMajor;
    }

    public void setLimitMajor(String limitMajor) {
        this.limitMajor = limitMajor == null ? null : limitMajor.trim();
    }

    public Integer getLimitGrade() {
        return limitGrade;
    }

    public void setLimitGrade(Integer limitGrade) {
        this.limitGrade = limitGrade;
    }

    public Integer getFitPeopleNum() {
        return fitPeopleNum;
    }

    public void setFitPeopleNum(Integer fitPeopleNum) {
        this.fitPeopleNum = fitPeopleNum;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName == null ? null : labName.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", address=").append(address);
        sb.append(", createTime=").append(createTime);
        sb.append(", creatorId=").append(creatorId);
        sb.append(", endTime=").append(endTime);
        sb.append(", experimentCondition=").append(experimentCondition);
        sb.append(", suggestGroupType=").append(suggestGroupType);
        sb.append(", examination=").append(examination);
        sb.append(", experimentType=").append(experimentType);
        sb.append(", achievementCheck=").append(achievementCheck);
        sb.append(", limitCollege=").append(limitCollege);
        sb.append(", limitMajor=").append(limitMajor);
        sb.append(", limitGrade=").append(limitGrade);
        sb.append(", fitPeopleNum=").append(fitPeopleNum);
        sb.append(", totalHours=").append(totalHours);
        sb.append(", labName=").append(labName);
        sb.append(", projectName=").append(projectName);
        sb.append(", projectType=").append(projectType);
        sb.append(", startTime=").append(startTime);
        sb.append(", status=").append(status);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
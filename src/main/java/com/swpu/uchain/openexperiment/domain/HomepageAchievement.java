package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;
import java.util.Date;

public class HomepageAchievement implements Serializable {
    private Integer id;

    private String realName;

    private String imgUrl;

    private String projectName;

    private String subordinateCollege;

    private Integer projectType;

    private Integer experimentType;

    private Date publishTime;

    private Date updateTime;

    private Integer status;

    private Date startDate;

    private Date endTime;

    private String content;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
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

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public Integer getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(Integer experimentType) {
        this.experimentType = experimentType;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", realName=").append(realName);
        sb.append(", imgUrl=").append(imgUrl);
        sb.append(", projectName=").append(projectName);
        sb.append(", subordinateCollege=").append(subordinateCollege);
        sb.append(", projectType=").append(projectType);
        sb.append(", experimentType=").append(experimentType);
        sb.append(", publishTime=").append(publishTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", status=").append(status);
        sb.append(", startDate=").append(startDate);
        sb.append(", endTime=").append(endTime);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
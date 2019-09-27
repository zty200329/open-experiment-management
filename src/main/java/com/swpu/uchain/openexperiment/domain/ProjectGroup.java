package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProjectGroup implements Serializable {
    private Long id;

    private String address;

    private Date createTime;

    private Long creatorId;

    private Date endTime;

    private String experimentCondition;

    private Integer suggestGroupType;

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
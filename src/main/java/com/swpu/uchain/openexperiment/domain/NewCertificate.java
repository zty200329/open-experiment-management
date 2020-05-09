package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;

public class NewCertificate implements Serializable {
    private Long id;

    private String serialNumber;

    private String projectName;

    private String projectType;

    private Long userId;

    private String memberRole;

    private String experimentType;

    private String subordinateCollage;

    private Boolean isTrue;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType == null ? null : projectType.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole == null ? null : memberRole.trim();
    }

    public String getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(String experimentType) {
        this.experimentType = experimentType == null ? null : experimentType.trim();
    }

    public String getSubordinateCollage() {
        return subordinateCollage;
    }

    public void setSubordinateCollage(String subordinateCollage) {
        this.subordinateCollage = subordinateCollage == null ? null : subordinateCollage.trim();
    }

    public Boolean getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(Boolean isTrue) {
        this.isTrue = isTrue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", serialNumber=").append(serialNumber);
        sb.append(", projectName=").append(projectName);
        sb.append(", projectType=").append(projectType);
        sb.append(", userId=").append(userId);
        sb.append(", memberRole=").append(memberRole);
        sb.append(", experimentType=").append(experimentType);
        sb.append(", subordinateCollage=").append(subordinateCollage);
        sb.append(", isTrue=").append(isTrue);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
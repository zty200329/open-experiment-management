package com.swpu.uchain.openexperiment.domain;

import com.swpu.uchain.openexperiment.accessctro.ExcelResources;

import java.io.Serializable;

public class NewCertificate implements Serializable {
    private Long id;

    private String name;

    private String serialNumber;

    private String projectName;

    private String projectType;

    private Long userId;

    private String memberRole;

    private String subordinateCollage;

    private Boolean isTrue;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ExcelResources(title = "姓名",order = 2)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    @ExcelResources(title = "立项编号",order = 1)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    @ExcelResources(title = "项目名称" ,order = 4)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    @ExcelResources(title = "项目类型" ,order = 5)
    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType == null ? null : projectType.trim();
    }

    @ExcelResources(title = "学号" ,order = 3)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @ExcelResources(title = "成员身份" ,order = 6)
    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole == null ? null : memberRole.trim();
    }

    @ExcelResources(title = "所属学院" ,order = 7)
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
        sb.append(", name=").append(name);
        sb.append(", serialNumber=").append(serialNumber);
        sb.append(", projectName=").append(projectName);
        sb.append(", projectType=").append(projectType);
        sb.append(", userId=").append(userId);
        sb.append(", memberRole=").append(memberRole);
        sb.append(", subordinateCollage=").append(subordinateCollage);
        sb.append(", isTrue=").append(isTrue);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
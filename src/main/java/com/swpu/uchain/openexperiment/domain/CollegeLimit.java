package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;

public class CollegeLimit implements Serializable {
    private Integer id;

    private Integer crrentQuantity;

    private Byte projectType;

    private Byte limitCollege;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCrrentQuantity() {
        return crrentQuantity;
    }

    public void setCrrentQuantity(Integer crrentQuantity) {
        this.crrentQuantity = crrentQuantity;
    }

    public Byte getProjectType() {
        return projectType;
    }

    public void setProjectType(Byte projectType) {
        this.projectType = projectType;
    }

    public Byte getLimitCollege() {
        return limitCollege;
    }

    public void setLimitCollege(Byte limitCollege) {
        this.limitCollege = limitCollege;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", crrentQuantity=").append(crrentQuantity);
        sb.append(", projectType=").append(projectType);
        sb.append(", limitCollege=").append(limitCollege);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
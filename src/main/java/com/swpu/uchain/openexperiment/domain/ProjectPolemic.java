package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;

public class ProjectPolemic implements Serializable {
    private Long id;

    private Long projectGroupId;

    private Long polemicGroupId;

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

    public Long getPolemicGroupId() {
        return polemicGroupId;
    }

    public void setPolemicGroupId(Long polemicGroupId) {
        this.polemicGroupId = polemicGroupId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", projectGroupId=").append(projectGroupId);
        sb.append(", polemicGroupId=").append(polemicGroupId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
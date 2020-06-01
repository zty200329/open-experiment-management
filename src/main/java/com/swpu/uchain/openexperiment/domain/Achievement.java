package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;
import java.util.Date;

public class Achievement implements Serializable {
    private Long id;

    private Integer value;

    private String provenance;

    private Date issuingTime;

    private String issuingName;

    private String gmtCreate;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance == null ? null : provenance.trim();
    }

    public Date getIssuingTime() {
        return issuingTime;
    }

    public void setIssuingTime(Date issuingTime) {
        this.issuingTime = issuingTime;
    }

    public String getIssuingName() {
        return issuingName;
    }

    public void setIssuingName(String issuingName) {
        this.issuingName = issuingName == null ? null : issuingName.trim();
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate == null ? null : gmtCreate.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", value=").append(value);
        sb.append(", provenance=").append(provenance);
        sb.append(", issuingTime=").append(issuingTime);
        sb.append(", issuingName=").append(issuingName);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
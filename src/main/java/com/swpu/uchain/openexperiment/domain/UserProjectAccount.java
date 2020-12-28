package com.swpu.uchain.openexperiment.domain;

import java.io.Serializable;

public class UserProjectAccount implements Serializable {
    private Integer id;

    private String college;

    private Integer generalNum;

    private Integer keyNum;

    private Integer userType;

    private String code;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college == null ? null : college.trim();
    }

    public Integer getGeneralNum() {
        return generalNum;
    }

    public void setGeneralNum(Integer generalNum) {
        this.generalNum = generalNum;
    }

    public Integer getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(Integer keyNum) {
        this.keyNum = keyNum;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", college=").append(college);
        sb.append(", generalNum=").append(generalNum);
        sb.append(", keyNum=").append(keyNum);
        sb.append(", userType=").append(userType);
        sb.append(", code=").append(code);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
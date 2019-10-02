package com.swpu.uchain.openexperiment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author panghu
 */
@Data
public class User implements Serializable {
    private Long id;

    private String code;

    private String email;

    private String fixPhone;

    private String idCard;

    private String mobilePhone;

    @JsonIgnore
    private String password;

    private String qqNum;

    private String realName;

    private String sex;

    private Integer userType;

    private String institute;

    private String major;

    private Integer grade;

    private String workUnit;

    private Integer classNum;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", email=").append(email);
        sb.append(", fixPhone=").append(fixPhone);
        sb.append(", idCard=").append(idCard);
        sb.append(", mobilePhone=").append(mobilePhone);
        sb.append(", password=").append(password);
        sb.append(", qqNum=").append(qqNum);
        sb.append(", realName=").append(realName);
        sb.append(", sex=").append(sex);
        sb.append(", userType=").append(userType);
        sb.append(", institute=").append(institute);
        sb.append(", major=").append(major);
        sb.append(", grade=").append(grade);
        sb.append(", workUnit=").append(workUnit);
        sb.append(", classNum=").append(classNum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
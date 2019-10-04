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

    /**
     * 用户类型：1.学生,2.教师,3教授,4.副教授
     */
    private Integer userType;

    private String institute;

    private String major;

    private Integer grade;

    private String workUnit;

    private Integer classNum;

    private static final long serialVersionUID = 1L;
}
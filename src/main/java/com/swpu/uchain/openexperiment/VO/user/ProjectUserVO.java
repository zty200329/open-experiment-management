package com.swpu.uchain.openexperiment.VO.user;

import lombok.Data;

import java.util.Date;

/**
 * 项目成员信息
 * @author panghu
 */
@Data
public class ProjectUserVO {

    private String qqNum;

    private String mobilePhone;

    private String sex;

    private String email;

    private String fixPhone;

    private String realName;

    private Integer grade;

    private String institute;

    private String major;

    private String userType;

    private Long code;

    private String technicalRole;

    private String workDivision;

    private Integer memberRole;

    private Date joinTime;

    private String personalJudge;
}

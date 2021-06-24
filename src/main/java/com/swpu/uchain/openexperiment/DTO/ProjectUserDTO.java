package com.swpu.uchain.openexperiment.DTO;

import lombok.Data;

import java.util.Date;

/**
 * @author panghu
 */
@Data
public class ProjectUserDTO {

    private String qqNum;

    private String mobilePhone;

    private String sex;

    private String realName;

    private Integer grade;

    private String institute;

    private String major;

    private Integer userType;

    /*----*/

    private Long code;

    private String technicalRole;

    private String workDivision;

    private String memberRole;

    private Date joinTime;

    private String personalJudge;

}

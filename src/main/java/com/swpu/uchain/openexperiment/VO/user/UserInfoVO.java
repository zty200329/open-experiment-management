package com.swpu.uchain.openexperiment.VO.user;

import lombok.Data;

/**
 * @Author: clf
 * @Date: 19-1-29
 * @Description:
 */
@Data
public class UserInfoVO {
    private Long id;

    private String code;

    private String email;

    private String fixPhone;

    private String idCard;

    private String mobilePhone;

    private String qqNum;

    private String realName;

    private String sex;

    private Integer userType;

    private String institute;

    private String major;

    private Integer grade;

    private String workUnit;

    private Integer classNum;

}

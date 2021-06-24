package com.swpu.uchain.openexperiment.VO.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 */
@Data
public class UserDetailVO implements Serializable {
    private Long id;
    private String sex;
    private String realName;
    private String major;
    private Integer grade;
    private String code;
    private String qqNum;
    private String mobilePhone;
}

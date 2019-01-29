package com.swpu.uchain.openexperiment.VO.user;

import lombok.Data;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 */
@Data
public class UserDetailVO {
    private Long id;
    private String sex;
    private String major;
    private Integer grade;
    private String code;
    private String qqNum;
    private String mobilePhone;
}

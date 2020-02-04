package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

/**
 * @author: panghu
 * @Description:
 * @Date: Created in 19:46 2020/2/4
 * @Modified By:
 */
@Data
public class Teacher {

    private String code;

    private String email;

    /**
     * 所属学院
     */
    private Integer institute;

    private String major;

    private String qqNum;

    private String mobilePhone;

    private String realName;

    private String sex;

    private String workUnit;


    /**
     * 用户类型：1.学生,2.教师,3教授,4.副教授
     */
    private Integer userType;


}

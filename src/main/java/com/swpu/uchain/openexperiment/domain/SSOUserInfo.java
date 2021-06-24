package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

/**
 * @author: panghu
 * @Description:
 * @Date: Created in 15:05 2020/2/3
 * @Modified By:
 */
@Data
public class SSOUserInfo {

    /**
     * 工号 教师 comsys_teaching_number；学生 comsys_student_number
     */
    private Long code;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 年级  comsys_gradecode
     */
    private String grade;

    /**
     * 院系名称 comsys_faculetyname
     */
    private String institutionName;

    /**
     * 所属学院ID comsys_faculetycode
     */
    private String institution;

    /**
     * 电话号码
     */
    private String mobilePhone;


    /**
     * 用户姓名 comsys_name
     */
    private String realName;

    /**
     * 性别 comsys_genders
     */
    private String sex;

}

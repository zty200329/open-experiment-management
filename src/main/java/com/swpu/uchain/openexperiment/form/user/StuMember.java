package com.swpu.uchain.openexperiment.form.user;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 学生成员信息
 *
 * @author dengg
 */
@Data
public class StuMember {

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生学号
     */
    private Long userId;


    /**
     * 分工
     */
    private String technicalRole;

    /**
     * 具体工作
     */
    private String workDivision;

    /**
     * 评价
     */
    private String personalJudge;

    /**
     * 成员角色
     */
    @Max(3)
    @Min(2)
    private Integer memberRole;
}

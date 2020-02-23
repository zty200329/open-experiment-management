package com.swpu.uchain.openexperiment.DTO;

import lombok.Data;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

import java.util.List;
import java.util.Map;

/**
 * @author: panghu
 * @Description: 从Session当中获取到的用户信息
 * @Date: Created in 15:56 2020/2/23
 * @Modified By:
 */
@Data
public class SessionUserInfo {


    /**角色集合
     *集合内元素为Map<String,String>类型
     *Map中详细信息为：
     *key：ROLECNNAME;value:角色中文名称
     *key: ROLEIDENTIFY;value:角色代码
     */
    private List<Map<String, String>> role;
    /**岗位集合
     *集合内元素为Map<String,String>类型
     *Map中详细信息为：
     *key：POSTNAME;value:岗位中文名称
     *key: POSTIDENTIFY;value:岗位代码
     */
    private List<Map<String, String>> post;
    /**部门集合
     *集合内元素为Map<String,String>类型
     *Map中详细信息为：
     *key：DEPARTMENTNAME;value:部门中文名称
     *key: DEPARTMENTIDENTIFY;value:部门代码
     */
    private List<Map<String, String>> department;
    /**
     * 学生院系名称
     */
    private String faculetyName;
    /**
     * 学生院系代码
     */
    private String faculetyCode;
    /**
     * 学生年级名称
     */
    private String gradeName;
    /**
     * 学生年级代码
     */
    private String gradeCode;
    /**
     * 学生专业名称
     */
    private String disciplineName;
    /**
     * 学生专业代码
     */
    private String disciplineCode;
    /**
     * 学生班级名称
     */
    private String className;
    /**
     * 学生班级代码
     */
    private String classCode;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 民族
     */
    private String national;
    /**
     * 性别
     */
    private String genders;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 1:学生;2:教工
     */
    private String userType;
    /**
     * 其它职位
     */
    private String otherPost;
    /**
     * 教育程度
     */
    private String educational;
    /**
     * 教工号
     */
    private String teachingNumber;
    /**
     * 学生号
     */
    private String studentNumber;
    /**
     * 用户姓名
     */
    private String studentName;

}

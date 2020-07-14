package com.swpu.uchain.openexperiment.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 错误信息提示
 */
@Getter
public enum CodeMsg {
    /**
     * 用户角色权限相关 1401-1500
     */
    AUTHENTICATION_ERROR(1401,"用户认证失败,请重新登录" ),
    PASSWORD_ERROR(1402, "密码错误"),
    PERMISSION_DENNY(1403,"无权进行该操作" ),
    VERIFY_CODE_ERROR(1404, "验证码错误"),
    SEND_CODE_ERROR(1405, "发送验证码失败"),
    ROLE_NAME_CANT_BE_NULL(1406, "角色名称不能名称为空" ),
    ROLE_HAD_EXIST(1407, "当前角色已经存在" ),
    ADMIN_CANT_DELETE(1408, "ADMIN角色不能被删除"),
    ADMIN_CANT_CHANGE(1409, "ADMIN角色不能修改"),
    USER_ROLE_HAD_EXIST(1410, "当前用户已配置该角色,不能重复添加" ),
    USER_NO_EXIST(1411, "用户不存在"),
    ACL_EXIST(1412, "ACL已存在" ),
    ACL_NOT_EXIST(1413,"ACL不存在"),
    ROLE_NOT_EXIST(1414, "当前角色不存在" ),
    NOT_FOUND(1415,"url错误,请求路径未找到" ),
    ILLEGAL_MEMBER_ROLE(1416, "非法角色,系统不存在当前角色值"),
    USER_INFORMATION_MATCH_ERROR(1417,"用户信息匹配错误"),
    ALREADY_APPLY(1417, "已经进行申请操作,无法再次操作,请勿重复操作"),
    ROLE_ACL_HAD_EXIST(1418, "当前角色已拥有该权限,不能重复添加" ),
    MAXIMUM_APPLICATION(1419,"该类型项目申请达到上限"),
    STUDENT_CANT_GAIN_THIS_PERMISSION(1420,"学生无权获得该权限"),
    USER_INFO_NOT_COMPLETE(1421,"用户信息不完整,请先完善用户信息"),
    ADD_USER_INFO_NOT_COMPLETE(1422,"添加学生信息不完整"),
    ADD_TEACHER_INFO_NOT_COMPLETE(1422,"添加教师信息不完整"),
    ADD_TEACHER_NOT_EXIST(1422,"添加教师不存在"),
    ONLY_TEACHER_CAN_APPLY(1423,"非教师不得进行项目申请"),
    DOES_NOT_HAVE_DEFAULT_PROJECT_PERMISSIONS(1424,"不具备内定项目权限"),
    THIS_USER_CANNOT_BE_A_TUTOR(1425,"该用户不能作为指导教师"),






    /**
     *  项目相关 1501-1600
     */
    PROJECT_STATUS_IS_NOT_DECLARE(1501,"项目状态不是申报状态"),
    PROJECT_IS_NOT_LAB_ALLOWED(1502,"项目非实验室审核通过状态"),
    PROJECT_STATUS_IS_NOT_CONCLUDED(1503,"项目非结题状态"),
    PROJECT_GROUP_INFO_CANT_CHANGE(1504, "当前项目状态不允许进行修改"),
    REACH_NUM_MAX(1505,"已经达到人数限制,无法进行申请" ),
    PROJECT_CURRENT_STATUS_ERROR(1506,"当前项目状态不支持该操作"),
    PROJECT_TYPE_NULL_ERROR(1507,"项目类型不能为空"),
    COLLEGE_TYPE_NULL_ERROR(1508,"学院类型不能为空，请补充用户学院信息"),
    USER_HAD_JOINED_CANT_REJECT(1509, "用户已经加入项目组或已被拒绝，无法进行拒绝操作"),
    ADD_PROJECT_GROUP_ERROR(1510, "创建项目组异常"),
    ADD_USER_JOIN_ERROR(1511, "用户加入项目异常"),
    STUDENT_CANT_APPLY(1512, "学生无法申请立项"),
    CURRENT_PROJECT_STATUS_ERROR(1513,"当前项目状态不支持该操作"),
    PROJECT_NOT_MODIFY_BY_FUNCTION_DEPARTMENT(1514,"项目未被职能部门修改"),
    LEADING_TEACHER_CONTAINS_ERROR(1515,"指导教师必须包含项目申请人"),
    TOPIC_IS_NOT_OPEN(1516,"选题选择开放,无法在申请立项时指定学生"),
    PROJECT_HAS_BEEN_REJECTED(1517,"项目已经被驳回"),
    PROJECT_GROUP_HAD_EXIST(1518,"该项目已存在,请修改项目名" ),
    PROJECT_GROUP_NOT_EXIST(1519, "项目组不存在" ),
    KEY_PROJECT_AMOUNT_LIMIT(1520,"重点项目上报数量超过最大限制"),
    PROJECT_AMOUNT_LIMIT(1521,"普通项目上报数量超过最大限制"),
    GENERAL_PROJECT_CANT_APPLY(1522,"普通项目不得进行重点项目申请"),
    KEY_PROJECT_APPLY_MATERIAL_EMPTY(1523,"证明材料为空"),
    PROJECT_USER_MAX_ERROR(1524,"项目组成员达到最大值"),
    FIT_PEOPLE_LIMIT_ERROR(1525,"添加人数不在适应人数范围内"),
    FIT_PEOPLE_ERROR(1526,"限选人数参数错误"),
    PROJECT_FIT_PEOPLE_ERROR(1527,"项目组人数不符合要求,无法通过"),
    CURRENT_MODIFY_PROJECT_TYPE_ERROR(1528,"当前修改项目非重点项目"),




    /**
     * 文件相关异常 1601-1700
     */
    FILE_EXIST(1601,"文件已存在" ),
    FILE_NOT_EXIST(1602,"文件不存在"),
    UPLOAD_CANT_BE_EMPTY(1603,"上传文件不能为空"),
    FORMAT_UNSUPPORTED(1604,"文件格式不支持" ),
    UPLOAD_ERROR(1605,"文件上传失败"),
    DIR_NOT_EXIST(1606,"文件夹不存在" ),
    FILE_OVERSIZE(1607,"文件过大"),
    DOWNLOAD_ERROR(1608,"下载失败" ),
    DELETE_FILE_ERROR(1609, "删除文件异常"),
    FILE_NAME_EMPTY_ERROR(1610,"文件名不能为空"),
    FILE_EMPTY_ERROR(1610,"文件不能为空"),
    FOREWORD_DOC_EMPTY_ERROR(1611,"前端文件生成失败"),
    PDF_CONVERT_ERROR(1612,"PDF生成失败"),



    NOT_MATCH_LIMIT(517, "不符合条件,无法参与项目" ),
    USER_HAD_JOINED(520,"用户已经加入该项目,不需要重复添加" ),
    USER_HAD_BEEN_REJECTED(520,"用户曾已经被拒绝,无法再次加入"),
    USER_NOT_APPLYING(521,"用户未申请,无法进行操作" ),
    ANNOUNCEMENT_NOT_EXIST(522,"公告信息不存在" ),
    USER_GROUP_NOT_EXIST(523,"用户并未加入该项目组" ),
    CANT_AIM_TEACHER(524, "不能指定指导老师担任任何角色"),
    GROUP_LEADER_EXIST(525, "项目组组长已经存在"),
    USER_NOT_IN_GROUP(526, "非法操作,不能操作非自己的项目组"),
    FUNDS_APPLY_ERROR(528, "资金申请异常"),
    FUNDS_AGREE_CANT_CHANGE(529, "资金已经同意无法进行修改"),


    /**
     * 其他异常 1801-1900
     */
    PARAM_CANT_BE_NULL(1801, "参数不能为空" ),
    ADD_ERROR(1802, "添加失败" ),
    UNKNOWN_ROLE_TYPE_AND_OPERATION_TYPE(1803,"未知角色类型和操作类型"),
    REQUEST_METHOD_ERROR(1804,"不支持%s的请求方式" ),
    SERVER_ERROR(1805,"服务器未知错误:%s" ),
    BIND_ERROR(1806,"参数校验错误:%s"),
    NOT_IN_VALID_TIME(1807,"操作不在允许的时间之内"),
    TIME_DEFINE_ERROR(1808,"时间设置错误"),
    PAGE_NUM_ERROR(1809, "分页数目异常"),
    INPUT_INFO_HAS_EXISTED(1810,"信息已经存在"),
    UPDATE_ERROR(1807, "更新数据库失败"),
    DELETE_ERROR(1808,"信息不存在，删除失败"),

    /**
     * 证书相关 1901-2000
     */
    SERVICE_NOT_ENABLED(1901,"证书申领服务未打开"),
    SERVICE_IS_OPEN(1902,"服务已经开启了"),
    INPUT_YEAR(1903,"请输入要开放申领的批次"),
    OPEN_ERROR(1904,"打开失败 请重试"),
    CLOSE_ERROR(1905,"关闭失败 请重试"),
    CHOICE_IS_NULL(1906,"选择不能为空"),
    USER_ID_LENGTH(1907,"学号长度不合法"),
    CANNOT_BE_CLEARED_WHILE_THE_SYSTEM_IS_OPEN(1908,"系统开放期间不能清除"),
    TABLE_IS_NOT_EMPTY(1909,"请先清空数据!");

    private Integer code;
    private String msg;

    CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}

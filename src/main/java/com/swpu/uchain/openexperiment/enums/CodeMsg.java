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
     *
     */
    AUTHENTICATION_ERROR(401,"用户认证失败,请重新登录" ),
    PASSWORD_ERROR(402, "密码错误"),
    PERMISSION_DENNY(403,"权限不足" ),
    NOT_FOUND(404,"url错误,请求路径未找到" ),
    REQUEST_METHOD_ERROR(550,"不支持%s的请求方式" ),
    SERVER_ERROR(500,"服务器未知错误:%s" ),
    BIND_ERROR(511,"参数校验错误:%s"),
    ADMIN_CANT_DELETE(501, "ADMIN角色不能被删除"),
    ADMIN_CANT_CHANGE(502, "ADMIN角色不能修改"),
    USER_NO_EXIST(503, "用户不存在"),
    VERIFY_CODE_ERROR(504, "验证码错误"),
    SEND_CODE_ERROR(505, "发送验证码失败"),
    ACL_EXIST(506, "ACL已存在" ),
    ACL_NOT_EXIST(507,"ACL不存在"),
    UPDATE_ERROR(508, "更新数据库失败"),
    ROLE_NAME_CANT_BE_NULL(509, "角色名称不能名称为空" ),
    ROLE_HAD_EXIST(510, "当前角色已经存在" ),
    ADD_ERROR(511, "添加失败" ),
    USER_ROLE_HAD_EXIST(512, "当前用户已配置该角色,不能重复添加" ),
    PARAM_CANT_BE_NULL(513, "参数不能为空" ),
    ROLE_NOT_EXIST(514, "当前角色不存在" ),
    ROLE_ACL_HAD_EXIST(515, "当前角色已拥有该权限,不能重复添加" ),
    PROJECT_GROUP_NOT_EXIST(516, "项目组不存在" ),
    NOT_MATCH_LIMIT(517, "不符合条件,无法参与项目" ),
    REACH_NUM_MAX(518,"已经达到人数限制,无法进行申请" ),
    PROJECT_GROUP_HAD_EXIST(519,"该项目已存在,请修改项目名" ),
    USER_HAD_JOINED(520,"用户已经加入该项目,不需要重复添加" ),
    USER_HAD_BEEN_REJECTED(520,"用户曾已经被拒绝,无法再次加入"),
    USER_NOT_APPLYING(521,"用户未申请,无法进行操作" ),
    ANNOUNCEMENT_NOT_EXIST(522,"公告信息不存在" ),
    USER_GROUP_NOT_EXIST(523,"用户并未加入项目组" ),
    CANT_AIM_TEACHER(524, "不能指定指导老师担任任何角色"),
    GROUP_LEADER_EXIST(525, "项目组组长已经存在"),
    USER_NOT_IN_GROUP(526, "非法操作,不能操作非自己的项目组"),
    ILLEGAL_MEMBER_ROLE(527, "非法角色,系统不存在当前角色值"),
    FUNDS_APPLY_ERROR(528, "资金申请异常"),
    FUNDS_AGREE_CANT_CHANGE(529, "资金已经同意无法进行修改"),
    PAGE_NUM_ERROR(530, "分页数目异常"),
    USER_HAD_JOINED_CANT_REJECT(531, "用户已经加入项目组或已被拒绝，无法进行拒绝操作"),
    PROJECT_GROUP_INFO_CANT_CHANGE(532, "项目非实验室上报状态，无法修改"),
    ADD_PROJECT_GROUP_ERROR(533, "创建项目组异常"),
    ADD_USER_JOIN_ERROR(534, "用户加入项目异常"),
    STUDENT_CANT_APPLY(535, "学生无法申请立项"),
    ALREADY_APPLY(536, "已经进行申请操作,无法再次操作,请勿重复操作"),
    PROJECT_IS_DECLARE(537,"项目还未审核，无法申请加入"),
    FILE_EXIST(601,"文件已存在" ),
    FILE_NOT_EXIST(602,"文件不存在"),
    UPLOAD_CANT_BE_EMPTY(603,"上传文件不能为空"),
    FORMAT_UNSUPPORTED(604,"文件格式不支持" ),
    UPLOAD_ERROR(605,"文件上传失败"),
    DIR_NOT_EXIST(606,"文件夹不存在" ),
    FILE_OVERSIZE(607,"文件过大"),
    DOWNLOAD_ERROR(608,"下载失败" ),
    DELETE_FILE_ERROR(609, "删除文件异常"),
    TIME_DEFINE_ERROR(610,"时间设置错误"),
    PROJECT_HAS_BEEN_REJECTED(611,"项目已经被驳回"),
    PROJECT_STATUS_IS_NOT_DECLARE(612,"项目状态不是申报状态"),
    PROJECT_STATUS_IS_NOT_LAB_ALLOWED_AND_REPORTED(613,"项目状态不是实验室上报状态"),
    PROJECT_NOT_MODIFY_BY_FUNCTION_DEPARTMENT(614,"项目未被职能部门修改"),
    LEADING_TEACHER_CONTAINS_ERROR(615,"指导教师必须包含项目申请人"),
    TOPIC_IS_NOT_OPEN(616,"选题选择开放,无法在申请立项时指定学生"),
    CURRENT_PROJECT_STATUS_ERROR(617,"当前项目不支持该操作"),
    USER_INFORMATION_MATCH_ERROR(618,"用户信息匹配错误");


    private Integer code;
    private String msg;

    CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}

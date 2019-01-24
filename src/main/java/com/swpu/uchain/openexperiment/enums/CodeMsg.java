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
    AUTHENTICATION_ERROR(401,"用户认证失败,请重新登录" ),
    PASSWORD_ERROR(402, "密码错误"),
    PERMISSION_DENNY(403,"权限不足" ),
    NOT_FOUND(404,"url错误,请求路径未找到" ),
    REQUEST_METHOD_ERROR(550,"不支持%s的请求方式" ),
    SERVER_ERROR(500,"服务器未知错误:%s" ),
    BIND_ERROR(511,"参数校验错误:%s"),
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
    NOT_MATCH_LIMIT(517, "不符合选择限制" ),
    REACH_NUM_MAX(518,"已经达到人数限制,无法进行申请" ),
    FILE_EXIST(601,"文件已存在" ),
    FILE_NOT_EXIST(602,"文件不存在"),
    NOT_BE_EMPTY(603,"上传文件不能为空"),
    FORMAT_UNSUPPORTED(604,"文件格式不支持" ),
    UPLOAD_ERROR(605,"文件上传失败"),
    DIR_NOT_EXIST(606,"文件夹不存在" ),
    FILE_OVERSIZE(607,"文件过大"),
    FILE_ALREADY_UPLOAD(608,"同一文件不能上传多次" );
    private Integer code;
    private String msg;

    CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

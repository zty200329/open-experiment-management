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
    ACL_EXIST(506,"ACL已存在" ),
    ACL_NOT_EXIST(507,"ACL不存在"),
    FILE_EXIST(601,"文件已存在" ),
    FILE_NOT_EXIST(602,"文件不存在"),
    NOT_BE_EMPTY(603,"上传文件不能为空"),
    FORMAT_UNSUPPORTED(604,"文件格式不支持" ),
    UPLOAD_ERROR(605,"文件上传失败"),
    DIR_NOT_EXIST(606,"文件夹不存在" ),
    FILE_OVERSIZE(607,"文件过大")
    ;

    private Integer code;
    private String msg;

    CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

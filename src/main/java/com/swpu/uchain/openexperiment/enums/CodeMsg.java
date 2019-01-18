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
    SEND_CODE_ERROR(505, "发送验证码失败");

    private Integer code;
    private String msg;

    CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

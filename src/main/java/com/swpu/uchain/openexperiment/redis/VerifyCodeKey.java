package com.swpu.uchain.openexperiment.redis;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 验证码的key封装
 */
public class VerifyCodeKey extends BasePrefix {

    public VerifyCodeKey(String prefix) {
        super(prefix);
    }

    public VerifyCodeKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static VerifyCodeKey getByClientIp = new VerifyCodeKey(240,"clientIp");
}

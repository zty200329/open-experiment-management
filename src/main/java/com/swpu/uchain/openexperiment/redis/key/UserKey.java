package com.swpu.uchain.openexperiment.redis.key;

/**
 * @Author: clf
 * @Date: 19-1-18
 * @Description:
 */
public class UserKey extends BasePrefix{

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static UserKey getUserByUserCode = new UserKey(300, "userCode");
}

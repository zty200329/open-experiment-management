package com.swpu.uchain.openexperiment.redis;

/**
 * @Author: clf
 * @Date: 19-1-18
 * @Description:
 */
public class UserKey extends BasePrefix{

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static UserKey userKey = new UserKey(300, "user");
}

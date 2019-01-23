package com.swpu.uchain.openexperiment.redis.key;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 */
public class UserProjectGroupKey extends BasePrefix{
    public UserProjectGroupKey(String prefix) {
        super(prefix);
    }

    public UserProjectGroupKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static UserProjectGroupKey getByProjectGroupIdAndUserId = new UserProjectGroupKey(120, "projectIdAndUserId");
}

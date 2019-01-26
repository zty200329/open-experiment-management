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

    public UserKey(String prefix){
        super(prefix);
    }
    public static KeyPrefix getByUserId = new UserKey("userId");
    public static UserKey getUserByUserCode = new UserKey(300, "userCode");
    public static UserKey getByKeyWord = new UserKey(120, "keyWord");
    public static UserKey getLeaderByGroupId = new UserKey(300, "leaderOfGroup");
}

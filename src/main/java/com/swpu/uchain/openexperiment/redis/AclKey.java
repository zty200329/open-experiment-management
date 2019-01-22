package com.swpu.uchain.openexperiment.redis;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 */
public class AclKey extends BasePrefix{
    public AclKey(String prefix) {
        super(prefix);
    }

    public AclKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static AclKey getByUserId = new AclKey(120 , "userId");
}

package com.swpu.uchain.openexperiment.redis.key;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 */
public class FundsKey extends BasePrefix{
    public FundsKey(String prefix) {
        super(prefix);
    }

    public FundsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static FundsKey getById = new FundsKey(120, "fundsId");
    public static FundsKey getByProjectGroupId = new FundsKey(300, "projectGroupId");
}

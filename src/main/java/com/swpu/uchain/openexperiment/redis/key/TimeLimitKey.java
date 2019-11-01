package com.swpu.uchain.openexperiment.redis.key;

public class TimeLimitKey extends BasePrefix {

    public TimeLimitKey(String prefix) {
        super(prefix);
    }

    public TimeLimitKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static TimeLimitKey getTimeLimitType = new  TimeLimitKey(-1,"getTimeLimitType");
}

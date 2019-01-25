package com.swpu.uchain.openexperiment.redis.key;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 */
public class ProjectGroupKey extends BasePrefix{
    public ProjectGroupKey(String prefix) {
        super(prefix);
    }

    public ProjectGroupKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


    public static ProjectGroupKey getByProjectGroupId = new ProjectGroupKey(120,"projectGroupId");
    public static ProjectGroupKey getByUserId = new ProjectGroupKey(300, "userId");
}

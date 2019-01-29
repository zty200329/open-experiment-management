package com.swpu.uchain.openexperiment.redis.key;

/**
 * @Description
 * @Author cby
 * @Date 19-1-22
 **/
public class FileKey extends BasePrefix {
    public FileKey(String prefix) {
        super(prefix);
    }
    public FileKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static FileKey getById = new FileKey("fileId");
}

package com.swpu.uchain.openexperiment.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author zty
 * @date 2020/11/01
 * mybatis二级缓存
 * 自定义Redis缓存实现
 */
@Slf4j
public class RedisCache implements Cache {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 当前放入缓存的mapper的namespace
     */
    private final String id;



    /**
     * 必须存在构造方法
     * @param id
     */
    public RedisCache(String id) {
        log.info("id:=====================> " + id);
        this.id = id;
    }

    /**
     *
     * 返回cache唯一标识
     */
    @Override
    public String getId() {
        return this.id;
    }


    /**
     * 缓存放入值  redis RedisTemplate   StringRedisTemplate
     */
    @Override
    public void putObject(Object key, Object value) {
        log.info("key:" + key.toString());
        log.info("value:" + value);
//      ishash类型作为缓存存储模型  key   hashkey  value
        redisTemplate.opsForHash().put(id.toString(),getKeyToMD5(key.toString()),value);



        if("com.swpu.uchain.openexperiment.mapper.NewsReleaseMapper".equals(id)){
            //缓存超时  client  用户   client  员工
            redisTemplate.expire(id.toString(),1, TimeUnit.HOURS);
        }


        if(id.equals("com.baizhi.dao.CityDAO")){
            //缓存超时  client  用户   client  员工
            redisTemplate.expire(id.toString(),30, TimeUnit.MINUTES);
        }

        //.....指定不同业务模块设置不同缓存超时时间




    }

    //获取中获取数据
    @Override
    public Object getObject(Object key) {
        System.out.println("key:" + key.toString());
//        //.setHashKeySerializer(new StringRedisSerializer());

        //根据key 从redis的hash类型中获取数据
        return redisTemplate.opsForHash().get(id.toString(), getKeyToMD5(key.toString()));
    }


    //注意:这个方法为mybatis保留方法 默认没有实现 后续版本可能会实现
    @Override
    public Object removeObject(Object key) {
        System.out.println("根据指定key删除缓存");
        return null;
    }

    @Override
    public void clear() {
        System.out.println("清空缓存~~~");
        //清空namespace
        redisTemplate.delete(id.toString());//清空缓存
    }

    //用来计算缓存数量
    @Override
    public int getSize() {
        //获取hash中key value数量
        return redisTemplate.opsForHash().size(id.toString()).intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    //封装一个对key进行md5处理方法
    private String getKeyToMD5(String key){
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}

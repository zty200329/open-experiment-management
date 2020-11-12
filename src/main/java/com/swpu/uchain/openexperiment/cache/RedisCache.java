package com.swpu.uchain.openexperiment.cache;

import com.swpu.uchain.openexperiment.util.ApplicationContextUtils;
import com.swpu.uchain.openexperiment.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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



    //当前放入缓存的mapper的namespace
    private final String id;

    //必须存在构造方法
    public RedisCache(String id) {
        log.info("id:=====================> " + id);
        this.id = id;
    }

    //返回cache唯一标识
    @Override
    public String getId() {
        return this.id;
    }


    //缓存放入值  redis RedisTemplate   StringRedisTemplate
    @Override
    public void putObject(Object key, Object value) {
        log.info("key:" + key.toString());
        log.info("value:" + value);
        //使用redishash类型作为缓存存储模型  key   hashkey  value
        getRedisTemplate().opsForHash().put(id.toString(),getKeyToMD5(key.toString()),value);



        if("com.swpu.uchain.openexperiment.mapper.NewsReleaseMapper".equals(id)){
            //缓存超时
            log.debug(id);
            getRedisTemplate().expire(id.toString(),1, TimeUnit.HOURS);
        }




        //.....指定不同业务模块设置不同缓存超时时间




    }

    //获取中获取数据
    @Override
    public Object getObject(Object key) {
        log.info("key:" + key.toString());

        //根据key 从redis的hash类型中获取数据
        return getRedisTemplate().opsForHash().get(id.toString(), getKeyToMD5(key.toString()));
    }


    //注意:这个方法为mybatis保留方法 默认没有实现 后续版本可能会实现
    @Override
    public Object removeObject(Object key) {
        log.info("根据指定key删除缓存");
        return null;
    }

    @Override
    public void clear() {
        log.info("清空缓存~~~");
        //清空namespace
        getRedisTemplate().delete(id.toString());//清空缓存
    }

    //用来计算缓存数量
    @Override
    public int getSize() {
        //获取hash中key value数量
        return getRedisTemplate().opsForHash().size(id.toString()).intValue();
    }
    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    //封装redisTemplate
    private RedisTemplate getRedisTemplate(){
        //通过application工具类获取redisTemplate
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    //封装一个对key进行md5处理方法
    private String getKeyToMD5(String key){
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}

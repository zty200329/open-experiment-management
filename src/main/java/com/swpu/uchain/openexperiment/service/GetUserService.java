package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.mapper.UserMapper;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.UserKey;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


/**
 * @author panghu
 * 获取User,不适用UserService是因为会出现引用依赖
 */
@Service
public class GetUserService {

    private UserMapper userMapper;

    private RedisService redisService;

    public GetUserService(UserMapper userMapper, RedisService redisService) {
        this.userMapper = userMapper;
        this.redisService = redisService;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        if (!"anonymousUser".equals(name)){
            return selectByUserCode(name);
        }
        return null;
    }

    public User selectByUserCode(String userCode) {
        User user = redisService.get(UserKey.getUserByUserCode, userCode, User.class);
        if (user != null){
            return user;
        }
        user = userMapper.selectByUserCode(userCode);
        if (user != null){
            redisService.set(UserKey.getUserByUserCode, userCode, user);
        }
        return user;
    }

}

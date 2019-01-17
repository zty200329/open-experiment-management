package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.UserMapper;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.form.LoginForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 用户登录实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean insert(User user) {
        if (userMapper.insert(user) == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        if (userMapper.updateByPrimaryKey(user) == 1){
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Result login(String clientIp, LoginForm loginForm) {
        //TODO
        return null;
    }

    @Override
    public String sendVerifyCode(String clientIp) throws IOException {
        //TODO
        return null;
    }

    @Override
    public boolean checkVerifyCode(String clientIp, String code) {
        //TODO
        return false;
    }

    @Override
    public User getCurrentUser() {
        //TODO
        return null;
    }

    @Override
    public User selectByUserCode(String userCode) {
        //TODO
        return null;
    }
}

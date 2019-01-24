package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/keyWord", name = "按关键字查找用户")
    public Object keyWord(String keyWord){
        return Result.success(userService.selectByKeyWord(keyWord));
    }

}

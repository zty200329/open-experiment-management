package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.user.UserUpdateForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 * 用户接口
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理口")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("按关键字查找用户")
    @PostMapping(value = "/keyWord", name = "按关键字查找用户")
    public Object keyWord(String keyWord){
        return Result.success(userService.selectByKeyWord(keyWord));
    }

    @ApiOperation("完善个人信息")
    @PostMapping(value = "updateUserInfo", name = "完善个人信息")
    public Object updateUserInfo(@Valid UserUpdateForm userUpdateForm){
        return userService.updateUserInfo(userUpdateForm);
    }

    @ApiOperation("查看个人基本信息")
    @GetMapping(value = "getMyInfo", name = "查看个人基本信息")
    public Object getMyInfo(){
        return userService.getMyInfo();
    }
}

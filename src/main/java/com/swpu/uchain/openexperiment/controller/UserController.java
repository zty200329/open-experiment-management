package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.domain.UserProjectGroup;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.MemberRole;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.user.UpdateUserCollegeForm;
import com.swpu.uchain.openexperiment.form.user.UserUpdateForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
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
@RequestMapping("/api/user")
@Api(tags = "用户管理口")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("按关键字查找用户")
    @PostMapping(value = "/keyWord", name = "按关键字查找用户")
    public Object keyWord(String keyWord, boolean isTeacher){
        return Result.success(userService.selectByKeyWord(keyWord, isTeacher));
    }

    @ApiOperation("完善个人信息")
    @PostMapping(value = "updateUserInfo", name = "完善个人信息")
    public Object updateUserInfo(@Valid @RequestBody UserUpdateForm userUpdateForm){
        return userService.updateUserInfo(userUpdateForm);
    }

    @ApiOperation("查看个人基本信息")
    @GetMapping(value = "getMyInfo", name = "查看个人基本信息")
    public Object getMyInfo(){
        return userService.getMyInfo();
    }

    @ApiOperation("管理员根据关键字获取用户信息")
    @GetMapping(value = "manageUsers", name = "管理员获取用户信息")
    public Object manageUsers(String keyWord){
        return userService.getManageUsersByKeyWord(keyWord);
    }

    @ApiOperation("根据用户工号查看用户信息")
    @GetMapping("/getUserInfoByUserId")
    public Result getUserInfoByUserId(Long userId){
        return userService.getUserInfoByUserId(userId);
    }

    @ApiOperation("根据用户工号查看学院和职称")
    @GetMapping("/getInfoByUserId")
    public Result getInfoByUserId(Long userId){
        return userService.getInfoByUserId(userId);
    }

    @ApiOperation("改变用户及其所属项目的学院")
    @PostMapping("/updateUserCollege")
    public Result updateUserCollege(@RequestBody @Valid UpdateUserCollegeForm updateUserCollegeForm){
        return userService.updateUserCollege(updateUserCollegeForm);
    }

    @ApiOperation("判断用户是否是项目组长")
    @PostMapping("/determineLeader")
    public Result determineLeader(Long projectGroupId){
        return userService.determineLeader(projectGroupId);
    }

}

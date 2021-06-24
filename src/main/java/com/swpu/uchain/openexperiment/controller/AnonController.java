package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.user.FirstLoginForm;
import com.swpu.uchain.openexperiment.form.user.GetAllPermissions;
import com.swpu.uchain.openexperiment.form.user.LoginChangeForm;
import com.swpu.uchain.openexperiment.form.user.LoginForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.UserService;
import com.swpu.uchain.openexperiment.util.ClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 匿名访问接口
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/anon")
@Api(tags = "匿名访问接口")
public class AnonController {
    @Autowired
    private UserService userService;



    @ApiOperation("登录接口")
    @PostMapping(value = "/loginFirst", name = "登录接口")
    public Object loginFirst(@Valid@RequestBody FirstLoginForm form, HttpServletRequest request){
        String ip = ClientUtil.getClientIpAddress(request);
        return userService.loginFirst(ip,form);
    }
    @ApiOperation("登录接口")
    @PostMapping(value = "/login", name = "登录接口")
    public Object login(@Valid@RequestBody LoginForm form, HttpServletRequest request){
        return userService.login(form);
    }

    @ApiOperation("登录接口")
    @PostMapping(value = "/loginChange", name = "登录接口")
    public Object loginChange(@Valid@RequestBody LoginChangeForm form){

        return userService.loginChange(form);
    }

    @ApiOperation("根据学号工号查看权限")
    @PostMapping(value = "/getAllPermissions")
    public Result getAllPermissions(@Valid@RequestBody GetAllPermissions getAllPermissions){
        return userService.getAllPermissions(getAllPermissions);

    }
    @ApiOperation("发送验证码接口")
    @GetMapping(value = "/sendVerifyCode", name = "发送验证码接口")
    public Object sendVerifyCode(HttpServletRequest request){
        String ip = ClientUtil.getClientIpAddress(request);
        String verifyCode;
        try {
            verifyCode = userService.sendVerifyCode(ip);
        } catch (IOException e) {
            return Result.error(CodeMsg.SEND_CODE_ERROR);
        }
        return Result.success(verifyCode);
    }
}

package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.LoginForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.UserService;
import com.swpu.uchain.openexperiment.util.ClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 匿名访问接口
 */
@RequestMapping("/anon")
@RestController
@Slf4j
public class AnonController {
    @Autowired
    private UserService userService;
    /**
     * 登录
     * @param loginForm
     * @param request
     * @return
     */
    @PostMapping(value = "/login", name = "登录接口")
    public Object login(@Valid LoginForm loginForm, HttpServletRequest request){
        String ip = ClientUtil.getClientIpAddress(request);
        return userService.login(ip, loginForm);
    }

    /**
     * 请求验证码
     * @param request
     * @return
     */
    @GetMapping(value = "/sendVerifyCode", name = "返送验证码接口")
    public Object sendVerifyCode(HttpServletRequest request){
        String ip = ClientUtil.getClientIpAddress(request);
        String verifyCode = null;
        try {
            verifyCode = userService.sendVerifyCode(ip);
        } catch (IOException e) {
            return Result.error(CodeMsg.SEND_CODE_ERROR);
        }
        return Result.success(verifyCode);
    }

    @GetMapping("/test")
    public Object test(HttpServletRequest request){
        return Result.success();
    }


}

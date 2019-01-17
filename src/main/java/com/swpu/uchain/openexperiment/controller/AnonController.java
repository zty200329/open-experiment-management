package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.LoginForm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 匿名访问接口
 */
@RequestMapping("/anon")
public class AnonController {
    /**
     * 登录
     * @param loginForm
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Object login(@Valid LoginForm loginForm, HttpServletRequest request){
        return null;
    }

    /**
     * 请求验证码
     * @param request
     * @return
     */
    @GetMapping("/sendVerifyCode")
    public Object sendVerifyCode(HttpServletRequest request){
        return null;
    }
}

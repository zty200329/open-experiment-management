package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: clf
 * @Date: 19-1-18
 * @Description:
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/url")
    public Object url(){
        return Result.success("测试成功");
    }

    @GetMapping("/permission")
    public Object test(){
        return Result.success();
    }
}

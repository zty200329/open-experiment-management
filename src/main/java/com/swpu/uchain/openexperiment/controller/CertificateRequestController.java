package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: zty
 * @Date: 2020/2/26 18:01
 */

@Slf4j
@CrossOrigin
@RequestMapping("/api/certificate")
@Api(tags = "证书申领接口")
public class CertificateRequestController {


    @ApiOperation("管理员处获取所有申领名单")
    @GetMapping(value = "/getList", name = "管理员处获取申领名单")
    public Result getList(){
        return null;
    }


    @ApiOperation("用户申领")
    @GetMapping(value = "/applyCertidicate")
    public Result applyCertidicate(){
        return null;
    }

}

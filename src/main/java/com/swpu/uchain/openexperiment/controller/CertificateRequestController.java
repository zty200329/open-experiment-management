package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.CertificateRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zty
 * @Date: 2020/2/26 18:01
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/certificate")
@Api(tags = "证书申领接口")
public class CertificateRequestController {

    @Autowired
    private CertificateRequestService certificateRequestService;

    @ApiOperation("判断是否开放申领")
    @GetMapping("/judge")
    public Result judgeInterface(){
        return certificateRequestService.judgeInterface();
    }

    @ApiOperation("管理员开启证书申领功能（将具有证书申领资格的同学查出存数据库）")
    @GetMapping(value = "/openRequest")
    public Result getAllList( Integer year){
        return certificateRequestService.getAllList(year);
    }

    @ApiOperation("学生获取自己能申请的项目列表")
    @GetMapping(value = "/getOwnCertificate")
    public Result getOwnCertificate(){
        return certificateRequestService.getOwnCertificate();
    }

    @ApiOperation("学生申领")
    @GetMapping(value = "/applyCertificate")
    public Result applyCertificate( Integer[] primaryKey){
        return certificateRequestService.choseCertificate(primaryKey);
    }

    @ApiOperation("管理员处获取所有已经申领的名单")
    @GetMapping(value = "/getFinalList", name = "管理员处获取申领名单")
    public Result getFinalList(Integer year){
        return certificateRequestService.getFinalList(year);
    }

    @ApiOperation("将需要的生成一份excel并下载")
    @GetMapping("/downloadList")
    public void downloadList(){
        return;
    }

    @ApiOperation("关闭开放")
    @GetMapping("/closeOpen")
    public Result closeOpen(){
        return certificateRequestService.closeOpen();
    }
}

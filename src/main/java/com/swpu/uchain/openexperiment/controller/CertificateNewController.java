package com.swpu.uchain.openexperiment.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swpu.uchain.openexperiment.form.certificate.ApplyCertificateForm;
import com.swpu.uchain.openexperiment.form.certificate.DeleteCertificateForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.CertificateNewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author zty
 * @date 2020/5/9 下午8:31
 * @description:
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/newCertificate")
@Api(tags = "证书申领接口(用这个)")
public class CertificateNewController {

    @Autowired
    private CertificateNewService certificateNewService;
    @ApiOperation("学生申领")
    @PostMapping(value = "/applyCertificate")
    public Result applyCertificate(@RequestBody @Valid ApplyCertificateForm applyCertificate){
        return certificateNewService.applyCertificate(applyCertificate);
    }

    @ApiOperation("查看自己申请的证书")
    @PostMapping(value = "/viewMyApplication")
    public Result viewMyApplication(){
        return certificateNewService.viewMyApplication();
    }

    @ApiOperation("删除自己申请的证书")
    @PostMapping(value = "/deleteMyApplication")
    public Result deleteMyApplication(@RequestBody DeleteCertificateForm deleteCertificate){
        return certificateNewService.deleteMyApplication(deleteCertificate);
    }

    @ApiOperation("管理员开启申请功能")
    @PostMapping("/openApply")
    public Result openApply(){
        return certificateNewService.openApply();
    }

    @ApiOperation("管理员关闭申请功能")
    @PostMapping("/closeApply")
    public Result closeApply(){
        return certificateNewService.closeApply();
    }

    @ApiOperation("将需要的生成一份excel并下载")
    @GetMapping("/downloadList")
    public Result downloadList(HttpServletResponse response){
        return certificateNewService.downloadList(response);
    }

    @ApiOperation("清空数据,关闭后和开启前进行 注意提示!")
    @GetMapping("/emptyTheTable")
    public Result emptyTheTable(){
        return certificateNewService.emptyTheTable();
    }
}

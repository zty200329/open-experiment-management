package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.certificate.ApplyCertificate;
import com.swpu.uchain.openexperiment.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("学生申领")
    @GetMapping(value = "/applyCertificate")
    public Result applyCertificate(ApplyCertificate applyCertificate){
        return null;
    }

}

package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: clf
 * @Date: 19-1-18
 * @Description:
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private ProjectFileService projectFileService;

    @GetMapping(value = "/url", name = "测试接口")
    public Object url() {
        return Result.success("测试成功");
    }

    @GetMapping(value = "/permission", name = "测试permission")
    public Object test() {
        return Result.success();
    }

    @PostMapping(value = "/uploadtest", name = "测试文件上传")
    public Object uploadTest(MultipartFile file) {
        return projectFileService.uploadFile(file);
    }

    @PostMapping(value = "/downloadtest", name = "测试文件下载")
    public Object downloadTest(String fileName, HttpServletResponse response) {
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        return projectFileService.downloadFile(fileName, response);
    }
}

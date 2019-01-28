package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.file.ReloadApplyForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import com.swpu.uchain.openexperiment.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author: clf
 * @Date: 19-1-28
 * @Description:
 * 项目文件管理模块
 */
@CrossOrigin
@RestController
@RequestMapping("/file")
@Api(tags = "文件管理接口")
public class FileController {
    @Autowired
    private ProjectFileService projectFileService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserProjectService userProjectService;

    @GetMapping(value = "/getApplyDoc", name = "下载立项申请正文doc")
    public void getApplyDoc(Long fileId, HttpServletResponse response){
        projectFileService.downloadApplyFile(fileId, response);
    }

    @GetMapping(value = "getApplyPdf", name = "下载立项申请正文的pdf")
    public void getApplyPdf(long fileId, HttpServletResponse response){
        projectFileService.downloadApplyPdf(fileId, response);
    }

    @PostMapping(value = "/reloadApplyDoc", name = "重新上传立项申请正文")
    public Object reloadApplyDoc(@Valid ReloadApplyForm reloadApplyForm){
        User currentUser = userService.getCurrentUser();
        if (userProjectService.selectByProjectGroupIdAndUserId(currentUser.getId(), reloadApplyForm.getProjectGroupId()) == null) {
            return Result.error(CodeMsg.PERMISSION_DENNY);
        }
        return projectFileService.uploadApplyDoc(reloadApplyForm.getFile(), reloadApplyForm.getProjectGroupId());
    }

}

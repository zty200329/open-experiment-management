package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.file.ConcludingReportForm;
import com.swpu.uchain.openexperiment.form.file.ReloadApplyForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import com.swpu.uchain.openexperiment.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private UserProjectService userProjectService;
    @Autowired
    private GetUserService getUserService;

    @ApiOperation("下载立项申请正文doc")
    @GetMapping(value = "/getApplyDoc", name = "下载立项申请正文doc")
    public void getApplyDoc(Long fileId, HttpServletResponse response){
        projectFileService.downloadApplyFile(fileId, response);
    }

    @ApiOperation("下载立项申请正文的pdf")
    @GetMapping(value = "getApplyPdf", name = "下载立项申请正文的pdf")
    public void getApplyPdf(long fileId, HttpServletResponse response){
        projectFileService.downloadApplyPdf(fileId, response);
    }

    @ApiOperation("重新上传立项申请正文")
    @PostMapping(value = "/reloadApplyDoc", name = "重新上传立项申请正文")
    public Object reloadApplyDoc(@Valid ReloadApplyForm reloadApplyForm){
        User currentUser = getUserService.getCurrentUser();
        if (userProjectService.selectByProjectGroupIdAndUserId(currentUser.getId(), reloadApplyForm.getProjectGroupId()) == null) {
            return Result.error(CodeMsg.PERMISSION_DENNY);
        }
        return projectFileService.uploadApplyDoc(reloadApplyForm.getFile(), reloadApplyForm.getProjectGroupId());
    }

    @ApiOperation("上传附件,一般由管理员进行上传")
    @PostMapping(value = "/uploadAttachmentFile", name = "上传附件")
    public Object uploadAttachmentFile(MultipartFile multipartFile){
        return projectFileService.uploadAttachmentFile(multipartFile);
    }

    @ApiOperation("上传结题报告")
    @PostMapping(value = "/uploadConcludingReport", name = "上传结题报告")
    public Object uploadConcludingReport(@Valid ConcludingReportForm concludingReportForm){
        return projectFileService.uploadConcludingReport(concludingReportForm);
    }

    @ApiOperation("显示所有附件信息")
    @GetMapping(value = "listAttachmentFiles", name = "显示所有附件信息")
    public Object listAttachmentFiles(){
        return projectFileService.listAttachmentFiles();
    }

    @PostMapping(value = "/deleteFile", name = "删除指定文件")
    public Object deleteFile(long fileId){
        projectFileService.delete(fileId);
        return Result.success();
    }

    @ApiOperation("下载附件")
    @GetMapping(value = "downloadAttachmentFile", name = "下载附件")
    public void downloadAttachmentFile(long fileId, HttpServletResponse response){
        projectFileService.downloadAttachmentFile(fileId, response);
    }

}

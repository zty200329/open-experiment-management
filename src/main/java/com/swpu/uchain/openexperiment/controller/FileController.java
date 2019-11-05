package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.file.ConcludingReportForm;
import com.swpu.uchain.openexperiment.form.file.ReloadApplyForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

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
    @Autowired
    private ProjectService projectService;

    @ApiOperation("下载重点立项申请正文pdf")
    @GetMapping(value = "/getApplyDoc", name = "下载重点立项申请正文pdf")
    public void getApplyDoc(Long fileId, HttpServletResponse response){
        projectFileService.downloadApplyFile(fileId, response);
    }

    @ApiOperation("下载结题正文")
    @GetMapping(value = "/getConclusionDoc")
    public void getConclusionDoc(Long fileId, HttpServletResponse response){
        projectFileService.getConclusionDoc(fileId, response);
    }

    @ApiIgnore
    @ApiOperation("下载立项申请正文的pdf")
    @GetMapping(value = "getApplyPdf", name = "下载立项申请正文的pdf")
    public void getApplyPdf(Long fileId, HttpServletResponse response){
        projectFileService.downloadApplyPdf(fileId, response);
    }

    @ApiOperation("上传立项申请正文")
    @PostMapping(value = "/reloadApplyDoc", name = "上传立项申请正文")
    public Object reloadApplyDoc(Long projectGroupId,MultipartFile file){
        User currentUser = getUserService.getCurrentUser();
        if (userProjectService.selectByProjectGroupIdAndUserId(projectGroupId,Long.valueOf(currentUser.getCode())) == null) {
            return Result.error(CodeMsg.PERMISSION_DENNY);
        }
        return projectFileService.uploadApplyDoc(file, projectGroupId);
    }



    @ApiIgnore("附件上传--6，普通项目结题验收报告；7，重点项目结题验收报告 8，普通项目实验报告")
    @PostMapping(value = "/uploadAttachmentFile", name = "上传附件")
    public Object uploadAttachmentFile(MultipartFile multipartFile,Integer attachmentType){
        return projectFileService.uploadAttachmentFile(multipartFile,attachmentType);
    }


    @ApiOperation("上传结题报告--重点项目和普通项目")
    @PostMapping(value = "/uploadConcludingReport", name = "上传结题报告")
    public Result uploadConcludingReport(Long projectId,MultipartFile file){
        return projectFileService.uploadConcludingReport(projectId,file);
    }

    @ApiIgnore
    @ApiOperation("显示所有附件信息")
    @GetMapping(value = "listAttachmentFiles", name = "显示所有附件信息")
    public Object listAttachmentFiles(){
        return projectFileService.listAttachmentFiles();
    }

    @ApiIgnore
    @PostMapping(value = "/deleteFile", name = "删除指定文件")
    public Object deleteFile(long fileId){
        projectFileService.delete(fileId);
        return Result.success();
    }

    @ApiIgnore("下载附件")
    @GetMapping(value = "downloadAttachmentFile", name = "下载附件")
    public void downloadAttachmentFile(long fileId, HttpServletResponse response){
        projectFileService.downloadAttachmentFile(fileId, response);
    }


    @ApiOperation("生成结题总览表")
    @PostMapping(value = "generateConclusionExcel", name = "生成结题总览表")
    public void generateConclusionExcel(HttpServletResponse response){
         projectService.generateConclusionExcel(response);
    }

    @ApiOperation("生成立项总览表")
    @PostMapping(value = "/generateEstablishExcel", name = "生成立项总览表")
    public void generateEstablishExcel(HttpServletResponse response){
        projectService.generateEstablishExcel(response);
    }
}

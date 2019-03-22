package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.AttachmentFileDTO;
import com.swpu.uchain.openexperiment.VO.file.AttachmentFileVO;
import com.swpu.uchain.openexperiment.config.UploadConfig;
import com.swpu.uchain.openexperiment.dao.ProjectFileMapper;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.FileType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.file.ConcludingReportForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.FileKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserService;
import com.swpu.uchain.openexperiment.service.xdoc.XDocService;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import com.swpu.uchain.openexperiment.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author cby
 * @Date 19-1-22
 **/
@Service
@Slf4j
@Component
public class ProjectFileServiceImpl implements ProjectFileService {
    @Autowired
    private UploadConfig uploadConfig;
    @Autowired
    private ProjectFileMapper projectFileMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private XDocService xDocService;

    @Override
    public boolean insert(ProjectFile projectFile) {
        if (projectFileMapper.insert(projectFile) == 1) {
            redisService.set(FileKey.getById, projectFile.getId() + "", projectFile);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(ProjectFile projectFile) {
        if (projectFileMapper.updateByPrimaryKey(projectFile) == 1) {
            redisService.set(FileKey.getById, projectFile.getId() + "", projectFile);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        ProjectFile projectFile = selectById(id);
        if (projectFile == null){
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        redisService.delete(FileKey.getById, id + "");
        if (FileUtil.deleteFile(FileUtil.getFileRealPath(
                projectFile.getId(),
                uploadConfig.getUploadDir(),
                projectFile.getFileName()))) {
            projectFileMapper.deleteByPrimaryKey(id);
        }
        throw new GlobalException(CodeMsg.DELETE_FILE_ERROR);
    }

    @Override
    public ProjectFile selectById(Long id) {
        ProjectFile projectFile = redisService.get(FileKey.getById, id + "", ProjectFile.class);
        if (projectFile == null){
            projectFile = projectFileMapper.selectByPrimaryKey(id);
            if (projectFile == null){
                redisService.set(FileKey.getById, id + "", projectFile);
            }
        }
        return projectFile;
    }

    @Override
    public ProjectFile getAimNameProjectFile(Long projectGroupId, String aimFileName){
        return projectFileMapper.selectByGroupIdFileName(projectGroupId, aimFileName);
    }

    @Override
    public Result uploadApplyDoc(MultipartFile file, Long projectGroupId) {
        ProjectFile mark = getAimNameProjectFile(projectGroupId, uploadConfig.getApplyFileName());
        if (mark != null) {
            File dest = new File(
                    FileUtil.getFileRealPath(mark.getId(),
                    uploadConfig.getUploadDir(),
                    uploadConfig.getApplyFileName()));
            dest.delete();
        }
        if (file.isEmpty()) {
            return Result.error(CodeMsg.UPLOAD_CANT_BE_EMPTY);
        }
        if (!checkFileFormat(file, FileType.WORD.getValue())) {
            return Result.error(CodeMsg.FORMAT_UNSUPPORTED);
        }
        User user = userService.getCurrentUser();
        //TODO,校验当前用户是否有权进行上传
        ProjectFile projectFile = new ProjectFile();
        projectFile.setUploadUserId(user.getId());
        projectFile.setFileType(FileType.WORD.getValue());
        projectFile.setFileName(uploadConfig.getApplyFileName());
        projectFile.setSize(FileUtil.FormatFileSize(file.getSize()));
        projectFile.setUploadTime(new Date());
        projectFile.setDownloadTimes(0);
        projectFile.setProjectGroupId(projectGroupId);
        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        if (FileUtil.uploadFile(
                file,
                FileUtil.getFileRealPath(
                    projectFile.getId(),
                    uploadConfig.getUploadDir(),
                    uploadConfig.getApplyFileName() + FileUtil.getMultipartFileSuffix(file)))) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPLOAD_ERROR);
    }

    @Override
    public void downloadApplyFile(Long fileId, HttpServletResponse response) {
        ProjectFile projectFile = projectFileMapper.selectByPrimaryKey(fileId);
        if (projectFile == null){
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        String realPath = FileUtil.getFileRealPath(fileId, uploadConfig.getUploadDir(), uploadConfig.getApplyFileName());
        if (!FileUtil.downloadFile(response, realPath)){
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }
        projectFile.setDownloadTimes(projectFile.getDownloadTimes() + 1);
        if (!update(projectFile)) {
            throw new GlobalException(CodeMsg.UPDATE_ERROR);
        }
    }

    @Override
    public void downloadApplyPdf(Long fileId, HttpServletResponse response) {
        ProjectFile projectFile = projectFileMapper.selectByPrimaryKey(fileId);
        if (projectFile == null){
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        String realPath = FileUtil.getFileRealPath(
                projectFile.getId(),
                uploadConfig.getUploadDir(),
                FileUtil.getFileNameWithoutSuffix(projectFile.getFileName()) + ".pdf");
        File file = new File(realPath);
        if (file.exists()){
            if (!FileUtil.downloadFile(response, realPath)) {
                throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
            }
        }
        try {
            xDocService.run(
                    FileUtil.getFileRealPath(projectFile.getId(), uploadConfig.getUploadDir(), projectFile.getFileName())
                    ,new File(realPath));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("======================XDoc文件类型转换异常=========================");
        }
        if (!FileUtil.downloadFile(response, realPath)) {
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }
    }

    @Override
    public List<ProjectFile> getProjectAllFiles(Long projectGroupId) {
        return projectFileMapper.selectByProjectGroupId(projectGroupId);
    }

    @Override
    public Result uploadAttachmentFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()){
            return Result.error(CodeMsg.UPLOAD_CANT_BE_EMPTY);
        }
        User currentUser = userService.getCurrentUser();
        ProjectFile projectFile = new ProjectFile();
        projectFile.setFileName(multipartFile.getOriginalFilename());
        projectFile.setDownloadTimes(0);
        projectFile.setFileType(FileUtil.getType(FileUtil.getMultipartFileSuffix(multipartFile)));
        projectFile.setSize(FileUtil.FormatFileSize(multipartFile.getSize()));
        projectFile.setUploadTime(new Date());
        projectFile.setUploadUserId(currentUser.getId());
        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        if (!FileUtil.uploadFile(
                multipartFile,
                FileUtil.getFileRealPath(
                        projectFile.getId(),
                        uploadConfig.getUploadDir(),
                        projectFile.getFileName()))) {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
        return Result.success();
    }

    @Override
    public void downloadAttachmentFile(long fileId, HttpServletResponse response) {
        ProjectFile projectFile = selectById(fileId);
        if (projectFile == null){
            throw new GlobalException(CodeMsg.FILE_NOT_EXIST);
        }
        if (!FileUtil.downloadFile(response, FileUtil.getFileRealPath(fileId, uploadConfig.getUploadDir(),projectFile.getFileName()))) {
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }
    }

    @Override
    public Result listAttachmentFiles() {
        List<AttachmentFileDTO> attachmentFileDTOS = projectFileMapper.selectAttachmentFiles();
        List<AttachmentFileVO> attachmentFileVOS = ConvertUtil.getAttachmentFileVOS(attachmentFileDTOS);
        return Result.success(attachmentFileVOS);
    }

    @Override
    public Result uploadConcludingReport(ConcludingReportForm concludingReportForm) {
        ProjectGroup projectGroup = projectService.selectByProjectGroupId(concludingReportForm.getProjectGroupId());
        if (projectGroup == null){
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        //TODO,判断项目组状态是否在结项状态下

        //判断是否存在该文件,若存在则进行覆盖
        ProjectFile projectFile = getAimNameProjectFile(projectGroup.getId(), uploadConfig.getConcludingFileName());
        if (projectFile != null){
            FileUtil.uploadFile(
                    concludingReportForm.getMultipartFile(),
                    FileUtil.getFileRealPath(
                            projectFile.getId(),
                            uploadConfig.getUploadDir(),
                            uploadConfig.getConcludingFileName()));
        }
        User currentUser = userService.getCurrentUser();
        //TODO,校验当前用户是否有权进行上传
        projectFile = new ProjectFile();
        projectFile.setUploadUserId(currentUser.getId());
        projectFile.setFileName(uploadConfig.getConcludingFileName());
        projectFile.setUploadTime(new Date());
        projectFile.setSize(FileUtil.FormatFileSize(concludingReportForm.getMultipartFile().getSize()));
        projectFile.setFileType(FileUtil.getType(FileUtil.getMultipartFileSuffix(concludingReportForm.getMultipartFile())));
        projectFile.setDownloadTimes(0);
        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        if (!FileUtil.uploadFile(
                concludingReportForm.getMultipartFile(),
                FileUtil.getFileRealPath(
                        projectFile.getId(),
                        uploadConfig.getUploadDir(),
                        uploadConfig.getConcludingFileName()))) {
            return Result.error(CodeMsg.UPLOAD_ERROR);
        }
        return Result.success();
    }

    public boolean checkFileFormat(MultipartFile multipartFile, Integer aimType){
        String suffix = FileUtil.getMultipartFileSuffix(multipartFile);
        int type = FileUtil.getType(suffix);
        if (type != aimType) {
            return false;
        }
        return true;
    }
}

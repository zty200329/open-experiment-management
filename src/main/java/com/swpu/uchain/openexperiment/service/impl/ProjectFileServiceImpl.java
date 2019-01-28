package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.ProjectFileMapper;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.FileType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import com.swpu.uchain.openexperiment.service.UserService;
import com.swpu.uchain.openexperiment.service.xdoc.XDocService;
import com.swpu.uchain.openexperiment.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${upload.upload-dir}")
    private String path;
    @Value("${upload.apply-file-name}")
    private String applyFileName;
    @Autowired
    private ProjectFileMapper projectFileMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private XDocService xDocService;

    @Override
    public boolean insert(ProjectFile projectFile) {
        return (projectFileMapper.insert(projectFile) == 1);
    }

    @Override
    public boolean update(ProjectFile projectFile) {
        return (projectFileMapper.updateByPrimaryKey(projectFile) == 1);
    }

    @Override
    public boolean delete(Long id) {
        return (projectFileMapper.deleteByPrimaryKey(id) == 1);
    }

    @Override
    public ProjectFile selectById(Long id) {
        return projectFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public Result uploadApplyDoc(MultipartFile file, Long projectGroupId) {
        ProjectFile mark = projectFileMapper.selectByGroupIdFileName(projectGroupId, applyFileName);
        if (mark != null) {
            File dest = new File(FileUtil.getFileRealPath(mark.getId(), path, applyFileName));
            dest.delete();
        }
        if (file.isEmpty()) {
            return Result.error(CodeMsg.NOT_BE_EMPTY);
        }
        String originalFilename = file.getOriginalFilename();
        //判断文件类型是否合法
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        int type = FileUtil.getType(suffix);
        if (type != FileType.WORD.getValue()) {
            return Result.error(CodeMsg.FORMAT_UNSUPPORTED);
        }
        User user = userService.getCurrentUser();
        ProjectFile projectFile = new ProjectFile();
        projectFile.setUploadUserId(user.getId());
        projectFile.setFileType(type);
        projectFile.setFileName(applyFileName);
        projectFile.setSize(FileUtil.FormatFileSize(file.getSize()));
        projectFile.setUploadTime(new Date());
        projectFile.setDownloadTimes(0);
        projectFile.setProjectGroupId(projectGroupId);
        if (!insert(projectFile)) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        if (FileUtil.uploadFile(file, FileUtil.getFileRealPath(projectFile.getId(), path, applyFileName))) {
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
        String realPath = FileUtil.getFileRealPath(fileId, path, applyFileName);
        if (!FileUtil.downloadFile(response, realPath)){
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
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
                path,
                FileUtil.getFileNameWithoutSuffix(projectFile.getFileName()) + ".pdf");
        File file = new File(realPath);
        if (file.exists()){
            if (!FileUtil.downloadFile(response, realPath)) {
                throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
            }
        }
        try {
            xDocService.run(
                    FileUtil.getFileRealPath(projectFile.getId(), path, projectFile.getFileName())
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
}

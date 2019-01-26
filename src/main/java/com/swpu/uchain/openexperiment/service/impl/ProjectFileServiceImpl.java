package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.ProjectFileMapper;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.file.UploadFileForm;
import com.swpu.uchain.openexperiment.redis.key.ProjectFileKey;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserService;
import com.swpu.uchain.openexperiment.util.FileTypeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
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

    @Value("${upload.file-name}")
    private String fileName;


    @Autowired
    private ProjectFileMapper projectFileMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;


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
    public List<String> getFileName(Long projectGroupId) {
        List<ProjectFile> projectFiles = projectFileMapper.getFileNameById(projectGroupId);
        List<String> fileNames = new ArrayList<>();
        for (ProjectFile projectFile : projectFiles) {
            fileNames.add(projectFile.getFileName());
        }
        return fileNames;
    }


    @Override
    public Result uploadFile(MultipartFile file, Long projectGroupId) {
        ProjectFile mark = projectFileMapper.selectByGroupIdAndKeyWord(projectGroupId, fileName);
        if (mark != null) {
            File dest = new File(path + "/" + mark.getFileName());
            dest.delete();
            delete(mark.getId());
            log.info(mark.getFileName() + " 文件被覆盖:");
        }

        if (file.isEmpty()) {
            return Result.error(CodeMsg.NOT_BE_EMPTY);
        }
        String originalFilename = file.getOriginalFilename();
        //判断文件类型是否合法
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        FileTypeUtil fileTypeUtil = new FileTypeUtil();
        int type = fileTypeUtil.getType(suffix);
        if (type == 0) {
            return Result.error(CodeMsg.FORMAT_UNSUPPORTED);
        }
        //判断文件大小是否合法
        ProjectFile projectFile = new ProjectFile();
        if (file.getSize() > (1024 * 10000)) {
            return Result.error(CodeMsg.FILE_OVERSIZE);
        }
        String size = "" + file.getSize();
        User user = userService.getCurrentUser();
        projectFile.setFileType(type);
        projectFile.setFileName(originalFilename);
        projectFile.setSize(size);
        projectFile.setUploadTime(new Date());
        projectFile.setUploadUserId(user.getId());
        projectFile.setDownloadTimes(0);
        projectFile.setProjectGroupId(projectGroupId);
        insert(projectFile);
        ProjectFile projectFile1 = projectFileMapper.selectByFileNameAndUploadId(originalFilename, user.getId());
        projectFile1.setFileName(projectFile1.getId() + "." + fileName + suffix);
        projectFileMapper.updateByPrimaryKey(projectFile1);

        File dest = new File(path + "/" + projectFile1.getFileName());
        //判断父目录是否存在
        if (!dest.getParentFile().exists()) {
            return Result.error(CodeMsg.DIR_NOT_EXIST);
        }
        try {
            file.transferTo(dest);
            log.info(user.getRealName() + "上传文件：" + projectFile1.getFileName());
            return Result.success(projectFile1);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }

    }

    @Override
    public Result downloadFile(String fileName, HttpServletResponse response) {
        String realPath = path + "";
        File file = new File(realPath, fileName);
        if (!file.exists()) {
            return Result.error(CodeMsg.FILE_NOT_EXIST);
        }
        if (file.exists()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer);
                    i = bis.read(buffer);
                }
                ProjectFile projectFile = projectFileMapper.selectByProjectName(fileName);
                projectFileMapper.updateFileDownloadTime(projectFile);
                return Result.success(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return Result.error(CodeMsg.DOWNLOAD_ERROR);
    }

    @Override
    public List<Long> getFileIdListByGroupId(Long projectGroupId) {
        return projectFileMapper.selectFileIdByProjectGroupId(projectGroupId);
    }
}

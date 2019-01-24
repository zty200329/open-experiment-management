package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.ProjectFileMapper;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.redis.key.ProjectFileKey;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import com.swpu.uchain.openexperiment.service.UserService;
import com.swpu.uchain.openexperiment.util.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @Description
 * @Author cby
 * @Date 19-1-22
 **/
@Service
@Slf4j
public class ProjectFileServiceImpl implements ProjectFileService {

    //文件上传路径
    private final Path path = Paths.get("upload_dir");

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
    public Result selectByProjectName(String projectName) {
        ProjectFile projectFile = redisService.get(ProjectFileKey.projectFileKey, projectName, ProjectFile.class);
        if (projectFile != null) {
            return Result.success(projectFile);
        }
        projectFile = projectFileMapper.selectByProjectName(projectName);
        if (projectFile != null) {
            redisService.set(ProjectFileKey.projectFileKey, projectName, projectFile);
        }
        return Result.success(projectFile);
    }

    @Override
    public Result insertFile(ProjectFile projectFile) {
        if (projectFileMapper.selectByProjectName(projectFile.getFileName()) != null) {
            return Result.error(CodeMsg.FILE_EXIST);
        }
        if (insert(projectFile)) {
            return Result.success(projectFile);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Result updateFile(ProjectFile projectFile) {
        if (projectFileMapper.selectByProjectName(projectFile.getFileName()) == null) {
            return Result.error(CodeMsg.FILE_NOT_EXIST);
        }
        if (update(projectFile)) {
            return Result.success(projectFile);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Result deleteFile(Long id) {
        if (projectFileMapper.selectByPrimaryKey(id) == null) {
            return Result.error(CodeMsg.FILE_NOT_EXIST);
        }
        if (delete(id)) {
            return Result.success();
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Result uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(CodeMsg.NOT_BE_EMPTY);
        }
        String fileName = file.getOriginalFilename();
        ProjectFile projectFile = new ProjectFile();
        String size = "" + file.getSize();
        if (file.getSize() > (1024*10000000)) {
            return Result.error(CodeMsg.FILE_OVERSIZE);
        }
        User user = userService.getCurrentUser();
        //TODO 同一用户不能上传相同文件
        if (projectFileMapper.selectByFileNameAndUploadId(fileName, user.getId()) != null) {
            return Result.error(CodeMsg.FILE_EXIST);
        }
        assert fileName != null;
        //获得文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        FileTypeUtil fileTypeUtil = new FileTypeUtil();
        int type = fileTypeUtil.getType(suffix);
        if (type == 0) {
            return Result.error(CodeMsg.FORMAT_UNSUPPORTED);
        }
        projectFile.setFileType(type);
        projectFile.setFileName(fileName);
        projectFile.setSize(size);
        projectFile.setUploadTime(new Date());
        projectFile.setUploadUserId(user.getId());
        projectFile.setDownloadTimes(0);
        File dest = new File(path + "/" + fileName);
        //判断父目录是否存在
        if (!dest.getParentFile().exists()) {
            return Result.error(CodeMsg.DIR_NOT_EXIST);
        }
        try {
            log.info(user.getRealName() + "上传文件：" + projectFile.getFileName());
            file.transferTo(dest);
            insert(projectFile);
            return Result.success(projectFile);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

    @Override
    public Result downloadFile(String fileName, HttpServletRequest request, HttpServletResponse response) {
        if (fileName != null) {
            String realPath = path + "";
            File file = new File(realPath, fileName);
            if (file.exists()) {
                // TODO 设置强制下载不打开
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream outputStream = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        outputStream.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    ProjectFile projectFile = projectFileMapper.selectByProjectName(fileName);
                    projectFileMapper.updateFileDownloadTime(fileName);
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
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }
}

package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.ProjectFileMapper;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.redis.ProjectFileKey;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectFileService;
import com.swpu.uchain.openexperiment.service.UserService;
import com.swpu.uchain.openexperiment.util.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        if (file.getSize() > 102400) {
            return Result.error(CodeMsg.FILE_OVERSIZE);
        }
        User user = userService.getCurrentUser();
        //同一用户不能上传相同文件
        if (user.getId().equals(projectFile.getUploadUserId()) &&
                projectFileMapper.selectByProjectName(fileName) != null) {
            return Result.error(CodeMsg.FILE_EXIST);
        }
        assert fileName != null;
        //获得文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
        FileTypeUtil fileTypeUtil = new FileTypeUtil();
        int type = fileTypeUtil.getType(suffix);
        if (type == 0) {
            return Result.error(CodeMsg.FORMAT_UNSUPPORTED);
        }
        projectFile.setFileType(type);
        projectFile.setFileName(fileName);
        projectFile.setSize(size);
        projectFile.setUploadTime(new Date());
        projectFile.setId(user.getId());
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
}

package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.result.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description
 * @Author cby
 * @Date 19-1-22
 **/
public interface ProjectFileService {

    /**
     * 添加文件
     * @param projectFile
     * @return:
     */
    boolean insert(ProjectFile projectFile);

    /**
     * 修改文件
     * @param projectFile
     * @return:
     */
    boolean update(ProjectFile projectFile);

    /**
     * 删除文件
     * @param id
     * @return:
     */
    boolean delete(Long id);

    /**
     * 按文件id进行查询
     * @param id
     * @return
     */
    ProjectFile selectById(Long id);

    /**
     * 上传立项申请正文
     * @param file
     * @param projectGroupId
     * @return:
     */
    Result uploadApplyDoc(MultipartFile file, Long projectGroupId);

    /**
     * 下载立项申请正文文件
     * @param fileId
     * @param response
     */
    void downloadApplyFile(Long fileId, HttpServletResponse response);

    /**
     * 获取立项申请的正文pdf文件
     * @param fileId
     * @param response
     */
    void downloadApplyPdf(Long fileId, HttpServletResponse response);

    /**
     * 获取项目相关的所有文件
     * @param projectGroupId
     * @return
     */
    List<ProjectFile> getProjectAllFiles(Long projectGroupId);
}

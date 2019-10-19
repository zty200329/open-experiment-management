package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.ProjectFile;
import com.swpu.uchain.openexperiment.form.file.ConcludingReportForm;
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
     */
    void delete(Long id);

    /**
     * 按文件id进行查询
     * @param id
     * @return
     */
    ProjectFile selectById(Long id);

    /**
     * 获取立项申请正文文件的ProjectFile对象
     * @param projectFileId
     * @param aimFileName
     * @return
     */
    ProjectFile getAimNameProjectFile(Long projectFileId, String aimFileName);

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

    /**
     * 上传附件
     * @param multipartFile
     * @return
     */
    Result uploadAttachmentFile(MultipartFile multipartFile,Integer attachmentType);

    /**
     * 下载附件
     * @param fileId
     * @param response
     */
    void downloadAttachmentFile(long fileId, HttpServletResponse response);

    /**
     * 获取所有附件信息
     * @return
     */
    Result listAttachmentFiles();

    /**
     * 上传结题报告文件
     * @return
     */
    Result uploadConcludingReport(Long projectId,MultipartFile file);
}

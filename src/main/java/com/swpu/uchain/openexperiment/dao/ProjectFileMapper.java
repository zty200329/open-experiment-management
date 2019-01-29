package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.DTO.AttachmentFileDTO;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectFileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectFile record);

    ProjectFile selectByPrimaryKey(Long id);

    List<ProjectFile> selectAll();

    int updateByPrimaryKey(ProjectFile record);

    int updateFileDownloadTime(ProjectFile file);

    ProjectFile selectByGroupIdFileName(Long projectGroupId, String fileName);

    List<ProjectFile> selectByProjectGroupId(Long projectGroupId);

    List<AttachmentFileDTO> selectAttachmentFiles();
}
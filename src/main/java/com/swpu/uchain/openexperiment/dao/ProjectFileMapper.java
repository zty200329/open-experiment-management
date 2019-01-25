package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.ProjectFile;

import java.util.List;

public interface ProjectFileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectFile record);

    ProjectFile selectByPrimaryKey(Long id);

    List<ProjectFile> selectAll();

    int updateByPrimaryKey(ProjectFile record);

    ProjectFile selectByProjectName(String fileName);

    int updateFileDownloadTime(ProjectFile file);

    ProjectFile selectByFileNameAndUploadId(String fileName,Long uploadUserId);

    List <Long>  selectFileIdByProjectGroupId(Long projectGroupId);
}
package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.AttachmentFileDTO;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProjectFileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectFile record);

    ProjectFile selectByPrimaryKey(Long id);

    List<ProjectFile> selectAll();

    int updateByPrimaryKey(ProjectFile record);

    int updateFileDownloadTime(ProjectFile file);

    ProjectFile selectByGroupIdFileName(Long projectGroupId, String fileName);

    ProjectFile selectByProjectGroupIdAndMaterialType(@Param("projectGroupId") Long projectGroupId,@Param("materialType") Integer materialType,@Param("filename")String fileName);

    ProjectFile selectByProjectGroupIdAndFileName(@Param("projectGroupId") Long projectGroupId,@Param("fileName") String fileName);

    List<AttachmentFileDTO> selectAttachmentFiles();
}
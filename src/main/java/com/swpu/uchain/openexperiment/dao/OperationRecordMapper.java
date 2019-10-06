package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.DTO.OperationRecordDTO;
import com.swpu.uchain.openexperiment.VO.project.ProjectHistoryInfoVO;
import com.swpu.uchain.openexperiment.domain.Announcement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author panghu
 */
@Repository
public interface OperationRecordMapper {

    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条数据
     * @param record 待插入的数据
     * @return
     */
    int insert(OperationRecordDTO record);

    /**
     * 多值插入
     * @param list 待插入的列表
     * @return
     */
    int multiInsert(List<OperationRecordDTO> list);

    int setNotVisibleByProjectId(@Param("projectId")Long projectId );

    ProjectHistoryInfoVO selectByPrimaryKey(Long id);

    List<ProjectHistoryInfoVO> selectAllByProjectId(@Param("projectId") Long projectId);

    int updateByPrimaryKey(ProjectHistoryInfoVO record);

}

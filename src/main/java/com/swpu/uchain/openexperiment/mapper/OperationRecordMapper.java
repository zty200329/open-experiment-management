package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.OperationRecord;
import com.swpu.uchain.openexperiment.DTO.ProjectHistoryInfo;
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
    int insert(OperationRecord record);

    /**
     * 多值插入
     * @param list 待插入的列表
     * @return
     */
    int multiInsert(List<OperationRecord> list);

    /**
     * 更具指定类型 设置记录不可见
     * type传入为空则更新所有
     * @param projectId 项目ID
     * @param type 操作类型
     * @return
     */
    int setNotVisibleByProjectId(@Param("projectId")Long projectId,@Param("type")Integer type);

    ProjectHistoryInfo selectByPrimaryKey(Long id);

    List<ProjectHistoryInfo> selectAllByProjectId(@Param("projectId") Long projectId);

    List<ProjectHistoryInfo> selectAllOfKeyProjectByProjectId(@Param("projectId") Long projectId);

    int updateByPrimaryKey(ProjectHistoryInfo record);

    List<ProjectHistoryInfo> selectAllByProjectIdList(@Param("list") List<Long> list);

    List<ProjectHistoryInfo> selectDesignatedTypeListByRelatedIdAndType(@Param("type")Integer type, @Param("relatedId")Long relatedId);

}

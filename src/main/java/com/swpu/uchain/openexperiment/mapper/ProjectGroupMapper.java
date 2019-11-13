package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.ConclusionDTO;
import com.swpu.uchain.openexperiment.VO.project.CheckProjectVO;
import com.swpu.uchain.openexperiment.VO.project.OpenTopicInfo;
import com.swpu.uchain.openexperiment.VO.project.ProjectTableInfo;
import com.swpu.uchain.openexperiment.VO.project.SelectProjectVO;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.VO.project.ProjectGroupDetailVO;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author panghu
 */
@Repository
public interface ProjectGroupMapper {

    String getIndexByCollege(@Param("college")Integer college);

    int deleteByPrimaryKey(Long id);

    int insert(ProjectGroup record);

    ProjectGroup selectByPrimaryKey(@Param("id") Long id);

    ProjectGroup selectByPrimaryProjectName(@Param("projectName") String projectName);

    List<ProjectGroup> selectAllByList(List<Long> list);

    int updateByPrimaryKey(ProjectGroup record);

    int setSerialNumberById(@Param("id") Long id,@Param("number")String serialNumber);

    ProjectGroup selectByName(String projectName);

    List<ProjectGroup> selectByUserIdAndStatus(@Param("userId") Long userId,@Param("projectStatus") Integer projectStatus);

    List<ProjectGroup> selectByCollegeIdAndStatus(@Param("college") String college,@Param("projectStatus") Integer projectStatus);


    List<CheckProjectVO> selectApplyOrderByTime(int projectStatus);

    List<SelectProjectVO> selectByFuzzyName(@Param("name") String name);

    List<ConclusionDTO> selectConclusionDTOs(@Param("college")Integer college);

    /**
     * 查询所有的公开选题的项目
     * @return
     */
    List<OpenTopicInfo> getAllOpenTopic();

    ProjectGroupDetailVO getProjectGroupDetailVOByProjectId(@Param("projectId")Long projectId);

    /**
     * 总览表项目信息
     * @param college
     * @return
     */
    List<ProjectTableInfo> getProjectTableInfoListByCollegeAndList(@Param("college") Integer college);

    int updateProjectStatus(@Param("id") Long projectId,@Param("status")Integer status);

    int updateProjectSerialNumber(@Param("id") Long projectId,@Param("serialNumber")String serialNumber);

    int updateProjectStatusOfList(@Param("list") List<Long> projectIdList,@Param("status")Integer status);

    int selectSpecifiedProjectList(@Param("list") List<Long> projectIdList,@Param("status")Integer status);

    List<Long> conditionQuery(QueryConditionForm form);

    List<Long> conditionQueryOfKeyProject(QueryConditionForm form);

    List<ProjectGroup> selectHistoricalInfoByUnitAndOperation(@Param("unit") Integer operationUnit,@Param("type") Integer operationType);

    List<ProjectGroup> selectKeyHistoricalInfoByUnitAndOperation(@Param("unit") Integer operationUnit,@Param("type") Integer operationType);

    Integer getCountOfSpecifiedStatusAndProjectProject(@Param("status") Integer status,@Param("college")Integer college);
}
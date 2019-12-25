package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.ConclusionDTO;
import com.swpu.uchain.openexperiment.VO.project.CheckProjectVO;
import com.swpu.uchain.openexperiment.VO.project.OpenTopicInfo;
import com.swpu.uchain.openexperiment.VO.project.ProjectTableInfo;
import com.swpu.uchain.openexperiment.VO.project.SelectProjectVO;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.VO.project.ProjectGroupDetailVO;
import com.swpu.uchain.openexperiment.enums.ProjectType;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author panghu
 */
@Repository
public interface ProjectGroupMapper {

    /**
     * 更新项目上传编号
     * @param projectId
     * @param serialNumber
     * @return
     */
    int updateProjectSerialNumber(@Param("id") Long projectId,@Param("serialNumber")String serialNumber);

    String getMaxSerialNumberByCollege(@Param("college")Integer college);

    String getMaxTempSerialNumberByCollege(@Param("college")Integer college);

    int updateProjectTempSerialNumber(@Param("id") Long projectId,@Param("tempSerialNumber")String serialNumber);


    int deleteByPrimaryKey(Long id);

    int insert(ProjectGroup record);

    ProjectGroup selectByPrimaryKey(@Param("id") Long id);

    ProjectGroup selectByPrimaryProjectName(@Param("projectName") String projectName);

    List<ProjectGroup> selectAllByList(List<Long> list);

    int updateByPrimaryKey(ProjectGroup record);

    ProjectGroup selectByName(String projectName);

    List<ProjectGroup>  selectByUserIdAndStatus(@Param("userId") Long userId,@Param("projectStatus") Integer projectStatus,@Param("status")Integer joinStatus);

    List<ProjectGroup> selectByCollegeIdAndStatus(@Param("college") String college,@Param("projectStatus") Integer projectStatus);

    List<CheckProjectVO> selectApplyOrderByTime(@Param("projectStatus") int projectStatus,@Param("projectType") Integer projectType,@Param("college") Integer college);

    List<SelectProjectVO> selectByFuzzyName(@Param("name") String name);

    List<ConclusionDTO> selectConclusionDTOs(@Param("college")Integer college);

    /**
     * 查询所有的公开选题的项目
     * @return
     */
    List<OpenTopicInfo> getAllOpenTopic(List<Long> list);

    ProjectGroupDetailVO getProjectGroupDetailVOByProjectId(@Param("projectId")Long projectId);

    /**
     * 总览表项目信息
     * @param college
     * @return
     */
    List<ProjectTableInfo> getProjectTableInfoListByCollegeAndList(@Param("college") Integer college,@Param("status")Integer status);

    int updateProjectStatus(@Param("id") Long projectId,@Param("status")Integer status);


    int updateProjectStatusOfList(@Param("list") List<Long> projectIdList,@Param("status")Integer status);

    int selectSpecifiedProjectList(@Param("list") List<Long> projectIdList,@Param("status")Integer status);

    List<Long> conditionQuery(QueryConditionForm form);

    List<Long> conditionQueryOfKeyProject(QueryConditionForm form);

    /**
     * 用于查询已经驳回的项目 --舍弃
     * @param operationUnit
     * @param operationType
     * @param college
     * @param type
     * @return
     */
    List<ProjectGroup> selectHistoricalInfoByUnitCollegeAndOperation(@Param("unit") Integer operationUnit,@Param("type") Integer operationType,
                                                                     @Param("college")Integer college,@Param("projectType")Integer type);

       /**
     * 获取普通的已通过的项目
     * @param college
     * @return
     */
    List<ProjectGroup> selectGeneralPassedProjectList(@Param("college")Integer college,@Param("status")Integer projectStatus);


    /**
     * 获取普通的已驳回的项目
     * @param college
     * @return
     */
    List<ProjectGroup> selectGeneralRejectedProjectList(@Param("college")Integer college,@Param("status")Integer projectStatus);



    /**
     * 获取重点已通过的项目
     */
    List<ProjectGroup> selectKeyPassedProjectList(@Param("college")Integer college,@Param("status")Integer projectStatus);

    /**
     * 获取被驳回的重点项目 --舍弃
     * @param operationUnit
     * @param operationType
     * @param college
     * @return
     */
    List<ProjectGroup> selectKeyHistoricalInfoByUnitAndOperation(@Param("unit") Integer operationUnit,@Param("type") Integer operationType,
                                                                 @Param("college")Integer college);



    Integer getCountOfSpecifiedStatusAndProjectProject(@Param("status") Integer status,@Param("college")Integer college);

    void updateProjectType(@Param("id") Long projectId,@Param("type") Integer type);
}
package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.VO.project.SelectByKeywordProjectVO;
import com.swpu.uchain.openexperiment.VO.project.SelectProjectVO;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.form.check.KeyProjectCheck;
import com.swpu.uchain.openexperiment.form.member.MemberQueryCondition;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import com.swpu.uchain.openexperiment.form.query.HistoryQueryProjectInfo;
import com.swpu.uchain.openexperiment.result.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-21
 * @Description: 项目管理模块
 */
public interface ProjectService {
    /**
     * 新增项目组
     *
     * @param projectGroup
     * @return
     */
    boolean insert(ProjectGroup projectGroup);

    /**
     * 更新项目组信息
     *
     * @param projectGroup
     * @return
     */
    boolean update(ProjectGroup projectGroup);

    /**
     * 删除项目组
     *
     * @param projectGroupId
     */
    void delete(Long projectGroupId);


    /**
     * 根据项目组id进行查找
     *
     * @param projectGroupId
     * @return
     */
    ProjectGroup selectByProjectGroupId(Long projectGroupId);

    /**
     * 获取某用户的某状态的项目组的信息
     *
     * @param userId
     * @param projectStatus
     * @return
     */
    List<ProjectGroup> selectByUserIdAndProjectStatus(Long userId, Integer projectStatus,Integer joinStatus);

    /**
     * 立项申请接口
     *
     * @param form 申请立项表单
     * @return 接口调用返回结果
     */
    Result applyCreateProject(CreateProjectApplyForm form);

    /**
     * 职能部门内定项目(暂时为普通)
     * @param form
     * @return
     */
    Result FunctionCreateCommonApply(FunctionCreateProjectApplyForm form);


    /**
     * 更新项目组信息
     *
     * @param updateProjectApplyForm
     * @return
     */
    Result applyUpdateProject(UpdateProjectApplyForm updateProjectApplyForm);

    /**
     * 指导教师删除学院审核之前的项目
     * @param list
     * @return
     */
    Result  instructorsToDeleteItems(List<ProjectCheckForm> list);

    /**
     * 获取当前用户的所有参与的项目
     *
     * @param projectStatus
     * @return
     */
    Result getCurrentUserProjects(Integer projectStatus,Integer joinStatus);

    /**
     * 同意用户加入项目
     *
     * @param joinForm
     * @return
     */
    Result agreeJoin(JoinForm[] joinForm);

    /**
     * 同意立项
     *
     * @param projectGroupIdList 同意项目ID列表
     * @return
     */
    Result agreeEstablish(List<ProjectCheckForm> projectGroupIdList);

    /**
     * 获取项目的立项申请信息
     *
     * @param projectGroupId
     * @return
     */
    Result getApplyForm(Long projectGroupId);

    /**
     * 项目组组长追加重点项目申请信息
     * @param appendApplyForm
     * @return
     */
//    Result appendCreateApply(AppendApplyForm appendApplyForm);

    /**
     * 获取所有待审核立项项目信息
     *
     * @return
     */
    Result getPendingApprovalProjectByLabAdministrator();

    Result getPendingApprovalProjectBySecondaryUnit();

    Result getPendingApprovalProjectByFunctionalDepartment();

    Result getConclusionProject();

    Result getAllGeneralProject();

    /**
     * 上报学院领导
     *
     * @param formList
     * @return
     */
    Result reportToCollegeLeader(List<ProjectCheckForm> formList);

    /**
     * 获取当前指导老师的项目成员审批列表
     *
     * @return
     */
    List getJoinInfo();

    /**
     * 拒绝某用户申请加入项目组
     *
     * @param joinForm
     * @return
     */
    Result rejectJoin(JoinForm[] joinForm);

    /**
     * 根据项目名搜索项目基本信息
     *
     * @param name
     * @return
     */
    List<SelectProjectVO> selectByProjectName(String name);

    /**
     * 驳回,修改立项申请项目信息
     *
     * @param formList 项目拒绝信息集合
     * @return
     */
    Result rejectProjectApplyByLabAdministrator(List<ProjectCheckForm> formList);

    /**
     * 学院领导上报到职能部门
     *
     * @param projectGroupIdList 项目组ID
     * @return 上报结果
     */
    Result reportToFunctionalDepartment(List<Long> projectGroupIdList);

    /**
     * 指导老师确认职能部门的修改
     *
     * @return
     */
    Result ensureOrNotModify(ConfirmForm confirmForm);

    /**
     * 获取当前用户的项目具体信息
     *
     * @param projectId 项目ID
     * @return
     */
    Result getProjectDetailById(Long projectId);


    /**
     * 学生获取所有可选立项选题
     *
     * @return
     */
    Result getAllOpenTopic();

    /**
     * 获取项目详情
     *
     * @param projectId
     * @return
     */
    Result getProjectGroupDetailVOByProjectId(Long projectId);

    /**
     * 普通项目结题状态获取项目详情
     * @param projectId
     * @return
     */
    Result getProjectGroupConclusionDetailByProjectId(Long projectId);

    Result getToBeReportedProjectBySecondaryUnit();

    Result getToReviewProject();

    Result getToBeReportedProjectByLabLeader();

    /**
     * 二级单位拒绝项目
     *
     * @param formList
     * @return
     */
    Result rejectProjectApplyBySecondaryUnit(List<ProjectCheckForm> formList);

    /**
     * 职能部门拒绝项目
     *
     * @param formList
     * @return
     */
    Result rejectProjectApplyByFunctionalDepartment(List<ProjectCheckForm> formList);

    Result approveProjectApplyByLabAdministrator(List<ProjectCheckForm> list);

    Result approveProjectApplyBySecondaryUnit(List<ProjectCheckForm> list);

    /**
     * 打分和推荐
     * @param
     * @return
     */
    Result collegeSetScore(List<CollegeGiveScore> collegeGiveScores);
    /**
     * 中期复核通过
     * @param list
     * @return
     */
    Result midTermReviewPassed(List<ProjectCheckForm> list);

    /**
     * 立项复核通过
     * @param list
     * @return
     */
    Result establishReviewPassed(List<ProjectCheckForm> list);

    /**
     * 学院复核通过
     * @param projectGradeList
     * @return
     */
    Result CollegeReviewPassed(List<ProjectGrade> projectGradeList);

    /**
     * 根据指定条件查询项目信息
     * @param form
     * @return
     */
    Result conditionallyQueryOfProject(QueryConditionForm form);

    /**
     * 查询历史信息
     * @param info
     * @return
     */
    Result getHistoricalProjectInfo(HistoryQueryProjectInfo info);

    Result getApplyingJoinInfoByCondition(MemberQueryCondition condition);

    Result addStudentToProject(JoinForm joinForm);

    Result removeStudentFromProject(JoinForm joinForm);

    Result cancelStudentFromProject(GenericId joinForm);

    Result getToBeConcludingProject();

    /**
     * 职能部门获取内定项目
     * @return
     */
    Result getFunctionCreateCommonApply();

    /**
     * 关键字查普通项目
     * @param Keyword
     * @return
     */
    Result selectByKeyword(String Keyword);

    Result selectConclusionByKeyword(String Keyword);

    /**
     * 关键字查重点项目
     * @param Keyword
     * @return
     */
    Result selectKeyProjectByKeyword(SelectByKeywordForm Keyword);

    Result selectConclusionKeyProjectByKeyword(SelectByKeywordForm Keyword);

    Result getIntermediateInspectionProject();

    /**
     * 学院获取待结题项目
     * @return
     */
    Result collegeGetsTheItemsToBeCompleted();

    /**
     * 学院获取该院同意结题项目
     * @return
     */
    Result collegeGetsTheProjects();

    /**
     * 学院获取职能部门同意的结题项目
     * @return
     */
    Result getTheSchoolHasCompletedProject();

    Result getMidTermReturnProject();

    Result getEstablishReturnProject();

    Result getCollegeReturnProject();

    Result getFunctionReturnProject();

    Result agreeIntermediateInspectionProject(List<ProjectCheckForm> list);

    Result agreeToBeConcludingProject(List<ProjectCheckForm> list);

    Result agreeCollegePassedTheExamination(List<ProjectCheckForm> list);

    /**
     *
     * @param list
     * @return
     */
    Result rejectIntermediateInspectionProject(List<ProjectCheckForm> list);

    /**
     * 学院初审不通过
     * @param list
     * @return
     */
    Result CollegeRejectToBeConcludingProject(List<ProjectCheckForm> list);

    Result rejectToBeConcludingProject(List<ProjectCheckForm> list);

    Result getAllOpenTopicByCondition(QueryConditionForm queryConditionForm);

    Result deleteMemberFromProject(Long projectId, Long userId);

    Result rejectProjectReportByLabAdministrator(List<ProjectCheckForm> formList);

    /**
     * 重点转为普通项目
     * @param formList
     * @return
     */
    Result changeKeyProjectToGeneral(List<ChangeKeyProjectToGeneralForm> formList);

    /**
     * 中期打回修改
     * @param list
     * @return
     */
    Result  midTermKeyProjectHitBack(List<ProjectCheckForm> list);

    /**
     * 职能部门立项打回修改
     * @param list
     * @return
     */
    Result  establishProjectHitBack(List<ProjectCheckForm> list);

    /**
     * 学院对普通项目给出评级
     * @param projectGradeList
     * @return
     */
    Result CollegeGivesRating(List<ProjectGrade> projectGradeList);

    /**
     * 学院初审退回修改
     * @param list
     * @return
     */
    Result CollegeHitBack(List<ProjectCheckForm> list);

    /**
     * 职能部门给出评级
     * @param list<ProjectCheckForm> list
     * @return
     */
    Result functionalGivesRating(List<ProjectCheckForm> list);

    /**
     * 职能部门审核打回
     * @param list
     * @return
     */
    Result functionHitBack(List<ProjectCheckForm> list);

    /**
     * 职能部门复核通过
     * @param list
     * @return
     */
    Result functionReviewPassed(List<ProjectCheckForm> list);
}

package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.form.query.HistoryQueryKeyProjectInfo;
import com.swpu.uchain.openexperiment.form.check.KeyProjectCheck;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import com.swpu.uchain.openexperiment.result.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @author dengg
 */
public interface KeyProjectService {

    /**
     * 重点项目申请
     * @param form 申请表单
     * @return
     */
    Result createKeyApply(KeyProjectApplyForm form);

    Result deleteKeyProject(List<KeyProjectCheck> list);
    /**
     * 指导教师获取带审批的重点项目申请表
     * @return
     */
    Result getKeyProjectApplyingListByGuideTeacher();

    Result insertIconicResult(List<IconicResultForm> iconicResultForms);

    Result deleteIconicResult(Long id);

    Result getKeyProjectApplyingListByLabAdmin();

    Result getKeyProjectApplyingListBySecondaryUnit();

    /**
     * 获取待评审的
     * @return
     */
    Result getToBeReviewedProject();
    /**
     * 学院获取待检查的重点项目
     * @return
     */
    Result getCollegeKeyProject ();

    Result getTheCollegeHasCompletedKeyProject();

    Result getTheSchoolHasCompletedKeyProject();

    Result getKeyProjectApplyingListByFunctionalDepartment();

    Result agreeKeyProjectByGuideTeacher(List<KeyProjectCheck> list);

    Result agreeKeyProjectByLabAdministrator(List<KeyProjectCheck> list);

    Result agreeKeyProjectBySecondaryUnit(List<KeyProjectCheck> list);

    Result agreeKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list);

    Result collegeSetKeyScore(List<CollegeGiveScore> collegeGiveScores);

    Result reportKeyProjectByLabAdministrator(List<KeyProjectCheck> list);

    Result reportKeyProjectBySecondaryUnit(List<KeyProjectCheck> list);

    Result rejectKeyProjectByLabAdministrator(List<KeyProjectCheck> list);

    Result rejectKeyProjectBySecondaryUnit(List<KeyProjectCheck> list);

    Result rejectKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list);

    Result rejectKeyProjectByGuideTeacher(List<KeyProjectCheck> list);

    Result getHistoricalKeyProjectInfo(HistoryQueryKeyProjectInfo info);

    Result getToBeReportedProjectByLabAdmin();

    Result getToBeReportedProjectBySecondaryUnit();

    Result getToReviewKeyProject();

    Result getKeyProjectDetailById(Long projectId);

    Result conditionallyQueryOfKeyProject(QueryConditionForm form);

    Result getIntermediateInspectionKeyProject(Integer college);

    Result getToBeConcludingKeyProject(Integer college);

    Result getCompleteKeyProject (Integer college);

    Result agreeIntermediateInspectionKeyProject(List<KeyProjectCheck> list);

    Result agreeToBeConcludingKeyProject(List<KeyProjectCheck> list);

    /**
     * 中期拒绝
     * @param list
     * @return
     */
    Result rejectIntermediateInspectionKeyProject(List<KeyProjectCheck> list);

    Result rejectToBeConcludingKeyProject(List<KeyProjectCheck> list);

    Result rejectKeyProjectReportBySecondaryUnit(List<KeyProjectCheck> list);

    /**
     * 重点项目打回
     * @param list
     * @return
     */
    Result midTermKeyProjectHitBack(List<KeyProjectCheck> list);

    /**
     * 学院打回
     * @param list
     * @return
     */
    Result collegeKeyProjectHitBack(List<KeyProjectCheck> list);

    /**
     * 职能部门退回修改
     * @param list
     * @return
     */
    Result functionKeyProjectHitBack(List<KeyProjectCheck> list);

    /**
     * 学院拒绝结题
     * @param list
     * @return
     */
    Result rejectCollegeKeyProject(List<KeyProjectCheck> list);

    /**
     * 学院给出等级
     * @param projectGradeList
     * @return
     */
    Result collegeGivesKeyProjectRating(List<ProjectGrade> projectGradeList);

    /**
     * 职能部门给出评级
     * @param projectGradeList
     * @return
     */
    Result functionGivesKeyProjectRating(List<ProjectGrade> projectGradeList);

    /**
     * 中期查看打回的列表
     * @return
     */
    Result getMidTermReturnProject();

    /**
     * 查看学院结题打回列表
     * @return
     */
    Result getCollegeReturnKeyProject();

    /**
     * 查看职能部门打回列表
     * @return
     */
    Result getFunctionReturnKeyProject(Integer college);

    /**
     * 中期复核通过
     * @param list
     * @return
     */
    Result midTermReviewPassed(List<KeyProjectCheck> list);

    /**
     * 学院复核通过
     * @param list
     * @return
     */
    Result collegeReviewPassed(List<KeyProjectCheck> list);

    /**
     * 职能部门
     * @param list
     * @return
     */
    Result functionReviewPassed(List<KeyProjectCheck> list);
}

package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.project.IconicResultForm;
import com.swpu.uchain.openexperiment.form.project.ProjectCheckForm;
import com.swpu.uchain.openexperiment.form.query.HistoryQueryKeyProjectInfo;
import com.swpu.uchain.openexperiment.form.check.KeyProjectCheck;
import com.swpu.uchain.openexperiment.form.project.KeyProjectApplyForm;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import com.swpu.uchain.openexperiment.result.Result;
import org.springframework.web.bind.annotation.RequestBody;

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

    /**
     * 指导教师获取带审批的重点项目申请表
     * @return
     */
    Result getKeyProjectApplyingListByGuideTeacher();

    Result insertIconicResult(List<IconicResultForm> iconicResultForms);

    Result getKeyProjectApplyingListByLabAdmin();

    Result getKeyProjectApplyingListBySecondaryUnit();

    Result getKeyProjectApplyingListByFunctionalDepartment();

    Result agreeKeyProjectByGuideTeacher(List<KeyProjectCheck> list);

    Result agreeKeyProjectByLabAdministrator(List<KeyProjectCheck> list);

    Result agreeKeyProjectBySecondaryUnit(List<KeyProjectCheck> list);

    Result agreeKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list);

    Result reportKeyProjectByLabAdministrator(List<KeyProjectCheck> list);

    Result reportKeyProjectBySecondaryUnit(List<KeyProjectCheck> list);

    Result rejectKeyProjectByLabAdministrator(List<KeyProjectCheck> list);

    Result rejectKeyProjectBySecondaryUnit(List<KeyProjectCheck> list);

    Result rejectKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list);

    Result rejectKeyProjectByGuideTeacher(List<KeyProjectCheck> list);

    Result getHistoricalKeyProjectInfo(HistoryQueryKeyProjectInfo info);

    Result getToBeReportedProjectByLabAdmin();

    Result getToBeReportedProjectBySecondaryUnit();

    Result getKeyProjectDetailById(Long projectId);

    Result conditionallyQueryOfKeyProject(QueryConditionForm form);

    Result getIntermediateInspectionKeyProject(Integer college);

    Result getToBeConcludingKeyProject(Integer college);

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
     * 查看打回的列表
     * @return
     */
    Result getMidTermReturnProject();

    /**
     * 中期复核通过
     * @param list
     * @return
     */
    Result midTermReviewPassed(List<KeyProjectCheck> list);
}

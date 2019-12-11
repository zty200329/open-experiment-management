package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.query.HistoryQueryKeyProjectInfo;
import com.swpu.uchain.openexperiment.form.check.KeyProjectCheck;
import com.swpu.uchain.openexperiment.form.project.KeyProjectApplyForm;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import com.swpu.uchain.openexperiment.result.Result;

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

    Result rejectIntermediateInspectionKeyProject(List<KeyProjectCheck> list);

    Result rejectToBeConcludingKeyProject(List<KeyProjectCheck> list);

    Result rejectKeyProjectReportBySecondaryUnit(List<KeyProjectCheck> list);
}

package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.amount.AmountLimitForm;
import com.swpu.uchain.openexperiment.form.amount.AmountSearchForm;
import com.swpu.uchain.openexperiment.form.amount.AmountUpdateForm;
import com.swpu.uchain.openexperiment.form.amount.ProjectApplyAmountLimitForm;
import com.swpu.uchain.openexperiment.result.Result;

import java.util.List;

/**
 * @author panghu
 */
public interface AmountLimitService {

    /**
     *
     *通过学院获取所有的项目数量限制信息
     * @param form
     * @return
     */
    Result getAmountLimitVOListByCollegeAndProjectType(AmountSearchForm form);

    /**
     * 批量设置项目数量限制信息
     * @param limitForms
     * @return
     */
    Result setAmount(List<AmountLimitForm> limitForms);

    /**
     * 获取所有的项目数量限制信息
     * @return
     */
    Result getAmountLimitList();

    /**
     * 更新项目数量限制信息
     * @param form
     * @return
     */
    Result  updateAmountLimit(AmountUpdateForm form);

    /**
     * 设置申请项目数量信息
     * @param form
     * @return
     */
    Result setApplyLimitAmount(ProjectApplyAmountLimitForm form);

    /**
     *
     * @param form 更新申请限制信息
     * @return
     */
    Result updateApplyLimitAmount(ProjectApplyAmountLimitForm form);

    /**
     * 获取所有项目申请限制
     * @return
     */
    Result getProjectApplyAmountLimit();
}

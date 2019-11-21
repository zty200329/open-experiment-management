package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.amount.AmountLimitForm;
import com.swpu.uchain.openexperiment.form.amount.AmountSearchForm;
import com.swpu.uchain.openexperiment.form.amount.AmountUpdateForm;
import com.swpu.uchain.openexperiment.form.amount.ProjectApplyAmountLimitForm;
import com.swpu.uchain.openexperiment.result.Result;

import java.util.List;

public interface AmountLimitService {

    Result getAmountLimitVOListByCollegeAndProjectType(AmountSearchForm form);

    Result setAmount(List<AmountLimitForm> limitForms);

    Result getAmountLimitList();

    Result  updateAmountLimit(AmountUpdateForm form);

    Result setApplyLimitAmount(ProjectApplyAmountLimitForm form);
}

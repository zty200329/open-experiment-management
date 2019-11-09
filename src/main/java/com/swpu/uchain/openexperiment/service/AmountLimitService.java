package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.amount.AmountLimitForm;
import com.swpu.uchain.openexperiment.form.amount.AmountSearchForm;
import com.swpu.uchain.openexperiment.form.amount.AmountUpdateForm;
import com.swpu.uchain.openexperiment.result.Result;

public interface AmountLimitService {

    Result getAmountLimitVOByCollegeAndProjectType(AmountSearchForm form);

    Result setAmount(AmountLimitForm form);

    Result getAmountLimitList();

    Result updateAmountLimit(AmountUpdateForm form);
}

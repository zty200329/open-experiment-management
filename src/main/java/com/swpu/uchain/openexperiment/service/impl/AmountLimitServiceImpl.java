package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.domain.AmountLimit;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.amount.AmountLimitForm;
import com.swpu.uchain.openexperiment.form.amount.AmountSearchForm;
import com.swpu.uchain.openexperiment.mapper.AmountLimitMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AmountLimitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dengg
 */
@Service
public class AmountLimitServiceImpl implements AmountLimitService {

    private AmountLimitMapper amountLimitMapper;

    @Autowired
    public AmountLimitServiceImpl(AmountLimitMapper amountLimitMapper) {
        this.amountLimitMapper = amountLimitMapper;
    }

    @Override
    public Result getAmountByCollegeAndUnit(AmountSearchForm form) {
        return Result.success(amountLimitMapper.getTimeLimitVOByCollegeAndUnit(form.getCollege(),form.getUnit()));
    }

    @Override
    public Result setAmount(AmountLimitForm form) {
        if (amountLimitMapper.getTimeLimitVOByCollegeAndUnit(form.getLimitCollege(),form.getLimitUnit()) != null){
            throw new GlobalException(CodeMsg.INPUT_INFO_HAS_EXISTED);
        }
        AmountLimit amountLimit = new AmountLimit();
        BeanUtils.copyProperties(form,amountLimit);
        amountLimitMapper.insertOne(amountLimit);
        return Result.success();
    }
}

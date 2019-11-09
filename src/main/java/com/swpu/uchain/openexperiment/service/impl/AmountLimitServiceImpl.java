package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.VO.limit.AmountLimitVO;
import com.swpu.uchain.openexperiment.domain.AmountLimit;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.amount.AmountLimitForm;
import com.swpu.uchain.openexperiment.form.amount.AmountSearchForm;
import com.swpu.uchain.openexperiment.form.amount.AmountUpdateForm;
import com.swpu.uchain.openexperiment.mapper.AmountLimitMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AmountLimitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return Result.success(amountLimitMapper.getAmountLimitVOByCollegeAndProjectType(form.getCollege(),form.getProjectType()));
    }

    @Override
    public Result setAmount(AmountLimitForm form) {
        if (amountLimitMapper.getAmountLimitVOByCollegeAndProjectType(form.getLimitCollege(),form.getProjectType()) != null){
            throw new GlobalException(CodeMsg.INPUT_INFO_HAS_EXISTED);
        }
        AmountLimit amountLimit = new AmountLimit();
        BeanUtils.copyProperties(form,amountLimit);
        amountLimitMapper.insertOne(amountLimit);
        return Result.success();
    }

    @Override
    public Result getAmountLimitList() {
        List<AmountLimitVO> limitList = amountLimitMapper.getAmountLimitVOByCollegeAndProjectType(null,null);
        return Result.success(limitList);
    }

    @Override
    public Result updateAmountLimit(AmountUpdateForm form) {
        int result = amountLimitMapper.updateTimeLimit(form.getId(),form.getMaxAmount(),form.getMinAmount());
        if (result != 1){
            throw new GlobalException(CodeMsg.UPDATE_ERROR);
        }
        return Result.success();
    }
}

package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.VO.limit.AmountLimitVO;
import com.swpu.uchain.openexperiment.domain.AmountLimit;
import com.swpu.uchain.openexperiment.domain.TimeLimit;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.TimeLimitType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.amount.AmountAndType;
import com.swpu.uchain.openexperiment.form.amount.AmountLimitForm;
import com.swpu.uchain.openexperiment.form.amount.AmountSearchForm;
import com.swpu.uchain.openexperiment.form.amount.AmountUpdateForm;
import com.swpu.uchain.openexperiment.mapper.AmountLimitMapper;
import com.swpu.uchain.openexperiment.mapper.TimeLimitMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AmountLimitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author dengg
 */
@Service
public class AmountLimitServiceImpl implements AmountLimitService {

    private AmountLimitMapper amountLimitMapper;

    private TimeLimitMapper timeLimitMapper;

    @Autowired
    public AmountLimitServiceImpl(AmountLimitMapper amountLimitMapper,TimeLimitMapper timeLimitMapper) {
        this.amountLimitMapper = amountLimitMapper;
        this.timeLimitMapper = timeLimitMapper;
    }

    @Override
    public Result getAmountLimitVOByCollegeAndProjectType(AmountSearchForm form) {
        return Result.success(amountLimitMapper.getAmountLimitVOByCollegeAndProjectType(form.getCollege(),form.getProjectType()));
    }

    @Override
    public Result setAmount(List<AmountLimitForm> limitForms) {

        List<TimeLimit> timeLimitList = new LinkedList<>();
        List<AmountLimit> amountLimitList = new LinkedList<>();

        for (AmountLimitForm form:limitForms
             ) {
            TimeLimit timeLimit = new TimeLimit();
            BeanUtils.copyProperties(form,timeLimit);
            timeLimit.setTimeLimitType(TimeLimitType.SECONDARY_UNIT_REPORT_LIMIT.getValue());
            timeLimitList.add(timeLimit);


            for (AmountAndType amountAndType:form.getList()
                 ) {
                AmountLimit amountLimit = new AmountLimit();
                amountLimit.setLimitCollege(form.getLimitCollege());
                amountLimit.setMaxAmount(amountAndType.getMaxAmount());
                amountLimit.setProjectType(amountAndType.getProjectType());
                amountLimitList.add(amountLimit);
            }

        }
        amountLimitMapper.multiInsert(amountLimitList);
        timeLimitMapper.multiInsert(timeLimitList);
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

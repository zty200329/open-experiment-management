package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.time.TimeLimitForm;
import com.swpu.uchain.openexperiment.mapper.TimeLimitMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.TimeLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dengg
 */
@Service
public class TimeLimitServiceImpl implements TimeLimitService {

    private TimeLimitMapper timeLimitMapper;

    @Autowired
    public TimeLimitServiceImpl(TimeLimitMapper timeLimitMapper) {
        this.timeLimitMapper = timeLimitMapper;
    }

    @Override
    public Result insert(TimeLimitForm form) {
        int result = timeLimitMapper.insert(form);
        if (result!=1){
            throw new GlobalException(CodeMsg.ADD_ERROR);
        }
        return Result.success();
    }

    @Override
    public Result update(TimeLimitForm form) {
        int result = timeLimitMapper.update(form);
        if (result!=1){
            throw new GlobalException(CodeMsg.UPDATE_ERROR );
        }
        return Result.success();
    }

    @Override
    public Result delete(Integer type) {
        return Result.success();
    }

    @Override
    public Result getTimeLimit(Integer type) {
        return Result.success(timeLimitMapper.getTimeLimitById(type));
    }
}

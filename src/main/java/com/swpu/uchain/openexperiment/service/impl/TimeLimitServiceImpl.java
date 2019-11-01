package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.domain.TimeLimit;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.TimeLimitType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.time.TimeLimitForm;
import com.swpu.uchain.openexperiment.mapper.TimeLimitMapper;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.TimeLimitKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.TimeLimitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author dengg
 */
@Service
public class TimeLimitServiceImpl implements TimeLimitService {

    private TimeLimitMapper timeLimitMapper;
    private RedisService redisService;

    @Autowired
    public TimeLimitServiceImpl(TimeLimitMapper timeLimitMapper, RedisService redisService) {
        this.timeLimitMapper = timeLimitMapper;
        this.redisService = redisService;
    }

    @Override
    public Result insert(TimeLimitForm form) {
        int result = timeLimitMapper.insert(form);
        TimeLimit timeLimit = new TimeLimit();
        BeanUtils.copyProperties(form,timeLimit);
        redisService.set(TimeLimitKey.getTimeLimitType,form.getTimeLimitType().toString(),timeLimit);
        if (result!=1){
            throw new GlobalException(CodeMsg.ADD_ERROR);
        }
        return Result.success();
    }

    @Override
    public Result update(TimeLimitForm form) {
        int result = timeLimitMapper.update(form);
        TimeLimit timeLimit = new TimeLimit();
        BeanUtils.copyProperties(form,timeLimit);
        redisService.set(TimeLimitKey.getTimeLimitType,form.getTimeLimitType().toString(),timeLimit);
        if (result!=1){
            throw new GlobalException(CodeMsg.UPDATE_ERROR );
        }
        return Result.success();
    }

    @Override
    public Result delete(Integer type) {
        redisService.delete(TimeLimitKey.getTimeLimitType,type.toString());
        timeLimitMapper.delete(type);
        return Result.success();
    }

    @Override
    public Result getTimeLimitByType(Integer type) {
        return Result.success(getTimeLimit(type));
    }

    public TimeLimit getTimeLimit(Integer type) {
        TimeLimit timeLimit = redisService.get(TimeLimitKey.getTimeLimitType,type.toString(),TimeLimit.class);
        if (timeLimit == null){
            timeLimit = timeLimitMapper.getTimeLimitById(type);
            if (timeLimit == null){
                return null;
            }else {
                redisService.set(TimeLimitKey.getTimeLimitType,type.toString(),timeLimit);
            }
        }
        return timeLimit;
    }

    public void validTime(TimeLimitType timeLimitType){
        Integer type = timeLimitType.getValue();
        TimeLimit timeLimit = getTimeLimit(type);
        Date startTime = timeLimit.getStartTime();
        Date endTime = timeLimit.getEndTime();
        Date currentTime = new Date();
        if (startTime.after(currentTime) || endTime.before(currentTime)){
            throw new GlobalException(CodeMsg.NOT_IN_VALID_TIME);
        }
    }
}

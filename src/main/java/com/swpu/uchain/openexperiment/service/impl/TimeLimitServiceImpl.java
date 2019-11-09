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
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.TimeLimitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dengg
 */
@Service
public class TimeLimitServiceImpl implements TimeLimitService {

    private TimeLimitMapper timeLimitMapper;
    private RedisService redisService;
    private GetUserService getUserService;

    @Autowired
    public TimeLimitServiceImpl(TimeLimitMapper timeLimitMapper, RedisService redisService,
                                GetUserService getUserService) {
        this.timeLimitMapper = timeLimitMapper;
        this.redisService = redisService;
        this.getUserService = getUserService;
    }

    @Override
    public Result insert(TimeLimitForm form) {
        TimeLimit timeLimit = new TimeLimit();
        Integer college = getUserService.getCurrentUser().getInstitute();
        BeanUtils.copyProperties(form,timeLimit);
        timeLimit.setLimitCollege(college);

        //添加元素，直接使用多值插入的方式插入数据库
        List<TimeLimit> timeLimitList = new ArrayList<>();
        timeLimitList.add(timeLimit);
        int result = timeLimitMapper.multiInsert(timeLimitList);
        BeanUtils.copyProperties(form,timeLimit);
        redisService.set(TimeLimitKey.getTimeLimitType,form.getTimeLimitType().toString(),timeLimit);
        if (result!=1){
            throw new GlobalException(CodeMsg.ADD_ERROR);
        }
        return Result.success();
    }

    @Override
    public Result update(TimeLimitForm form) {
        TimeLimit timeLimit = new TimeLimit();
        BeanUtils.copyProperties(form,timeLimit);
        timeLimit.setLimitCollege(getUserService.getCurrentUser().getInstitute()); // TODO 判断用户
        int result = timeLimitMapper.update(timeLimit);
        redisService.set(TimeLimitKey.getTimeLimitType,form.getTimeLimitType().toString(),timeLimit);
        if (result!=1){
            throw new GlobalException(CodeMsg.UPDATE_ERROR );
        }
        return Result.success();
    }

    @Override
    public Result delete(Integer type) {
        if (type == null){
            throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
        }
        redisService.delete(TimeLimitKey.getTimeLimitType,type.toString());
        timeLimitMapper.delete(type);
        return Result.success();
    }

    @Override
    public Result getTimeLimitByType(Integer type) {
        return Result.success(getTimeLimit(type));
    }

    public TimeLimit getTimeLimit(Integer type) {
        if (type == null){
            throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
        }
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
        //如果不存在验证直接退出
        if (timeLimit == null){
            return;
        }
        Date startTime = timeLimit.getStartTime();
        Date endTime = timeLimit.getEndTime();
        Date currentTime = new Date();
        //验证不通过，抛出自定义异常并捕捉
        if (startTime.after(currentTime) || endTime.before(currentTime)){
            throw new GlobalException(CodeMsg.NOT_IN_VALID_TIME);
        }
    }
}

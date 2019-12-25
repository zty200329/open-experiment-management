package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.domain.TimeLimit;
import com.swpu.uchain.openexperiment.domain.UserRole;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.CollegeType;
import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.enums.TimeLimitType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.time.TimeLimitForm;
import com.swpu.uchain.openexperiment.mapper.TimeLimitMapper;
import com.swpu.uchain.openexperiment.mapper.UserRoleMapper;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.TimeLimitKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.TimeLimitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserRoleMapper userRoleMapper;

    @Autowired
    public TimeLimitServiceImpl(TimeLimitMapper timeLimitMapper, RedisService redisService,
                                GetUserService getUserService,UserRoleMapper userRoleMapper) {
        this.timeLimitMapper = timeLimitMapper;
        this.redisService = redisService;
        this.getUserService = getUserService;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public Result insert(TimeLimitForm form) {
        TimeLimit timeLimit = new TimeLimit();
        Integer college = getUserService.getCurrentUser().getInstitute();
        BeanUtils.copyProperties(form,timeLimit);
        timeLimit.setLimitCollege(college);

        TimeLimit timeLimit1 = timeLimitMapper.getTimeLimitByTypeAndCollege(timeLimit.getTimeLimitType(),college);
        if (timeLimit1 != null) {
            throw new GlobalException(CodeMsg.INPUT_INFO_HAS_EXISTED);
        }

        //添加元素，直接使用多值插入的方式插入数据库
        List<TimeLimit> timeLimitList = new ArrayList<>();
        timeLimitList.add(timeLimit);
        int result = timeLimitMapper.multiInsert(timeLimitList);
        BeanUtils.copyProperties(form,timeLimit);
        if (result!=timeLimitList.size()){
            throw new GlobalException(CodeMsg.INPUT_INFO_HAS_EXISTED);
        }
        return Result.success();
    }

    @Override
    public Result update(TimeLimitForm form) {
        Long userId = Long.valueOf(getUserService.getCurrentUser().getCode());
        if (!userRoleMapper.selectByUserId(userId).getRoleId().equals(RoleType.FUNCTIONAL_DEPARTMENT.getValue())
        &&form.getTimeLimitType() >= TimeLimitType.SECONDARY_UNIT_CHECK_LIMIT.getValue()) {
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }
        TimeLimit timeLimit = new TimeLimit();
        BeanUtils.copyProperties(form,timeLimit);
        timeLimit.setLimitCollege(getUserService.getCurrentUser().getInstitute());
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
    public Result getTimeLimitList() {
        Integer college = getUserService.getCurrentUser().getInstitute();
        return Result.success(timeLimitMapper.getAllByCollege(college));
    }

    public TimeLimit getTimeLimitByType(Integer type, Integer college) {
        if (type == null || college == null) {
            throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return timeLimitMapper.getTimeLimitByTypeAndCollege(type,college);
    }

    @Override
    public void validTime(TimeLimitType timeLimitType){
        Integer type = timeLimitType.getValue();
        Integer college = getUserService.getCurrentUser().getInstitute();
        if (college == null){
            throw new GlobalException(CodeMsg.COLLEGE_TYPE_NULL_ERROR);
        }
        TimeLimit timeLimit = getTimeLimitByType(type,college);
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

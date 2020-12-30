package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.VO.limit.AmountLimitVO;
import com.swpu.uchain.openexperiment.domain.AmountLimit;
import com.swpu.uchain.openexperiment.domain.TimeLimit;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.domain.UserRole;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.enums.TimeLimitType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.amount.*;
import com.swpu.uchain.openexperiment.mapper.AmountLimitMapper;
import com.swpu.uchain.openexperiment.mapper.TimeLimitMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AmountLimitService;
import com.swpu.uchain.openexperiment.service.GetUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private GetUserService getUserService;

    @Autowired
    public AmountLimitServiceImpl(AmountLimitMapper amountLimitMapper,TimeLimitMapper timeLimitMapper) {
        this.amountLimitMapper = amountLimitMapper;
        this.timeLimitMapper = timeLimitMapper;
    }

    @Override
    public Result getAmountLimitVOListByCollegeAndProjectType(AmountSearchForm form) {
        User user = getUserService.getCurrentUser();
        return Result.success(amountLimitMapper.getAmountLimitVOListByCollegeAndProjectType(user.getInstitute(),form.getProjectType(),RoleType.SECONDARY_UNIT.getValue()));
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
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
            if (amountLimitMapper.getAmountLimitVOListByCollegeAndProjectType(form.getLimitCollege(),amountAndType.getProjectType(),RoleType.SECONDARY_UNIT.getValue()).size() != 0){
                throw new GlobalException(CodeMsg.INPUT_INFO_HAS_EXISTED);
            }
            AmountLimit amountLimit = new AmountLimit();
            amountLimit.setLimitCollege(form.getLimitCollege());
            amountLimit.setMaxAmount(amountAndType.getMaxAmount());
            amountLimit.setProjectType(amountAndType.getProjectType());
            amountLimit.setLimitUnit(RoleType.SECONDARY_UNIT.getValue());
            amountLimitList.add(amountLimit);
        }

        }
        int resultOfAmountInsert = amountLimitMapper.multiInsert(amountLimitList);
        int resultOfTimeLimitInsert = timeLimitMapper.multiInsert(timeLimitList);
        if (resultOfAmountInsert != amountLimitList.size() || resultOfTimeLimitInsert != timeLimitList.size()) {
            throw new GlobalException(CodeMsg.INPUT_INFO_HAS_EXISTED);
        }
        return Result.success();
    }

    @Override
    public Result getAmountLimitList() {
        List<AmountLimitVO> limitList = amountLimitMapper.getAmountLimitVOListByCollegeAndProjectType(null,null,RoleType.SECONDARY_UNIT.getValue());
        return Result.success(limitList);
    }

    /**
     * 更新项目数量限制
     * @param form
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Result updateAmountLimit(AmountUpdateForm form) {
        for (AmountAndType amountAndType:form.getList()
             ) {
            AmountLimit amountLimit = new AmountLimit();
            BeanUtils.copyProperties(amountAndType,amountLimit);
            amountLimit.setLimitCollege(form.getCollege());

            int result;
            //不存在则创建
            if (amountLimit.getId() == null) {
                result = amountLimitMapper.insertOne(amountLimit);
            }else {
                result = amountLimitMapper.updateTimeLimit(amountLimit.getId(),amountLimit.getMaxAmount());
            }
//            if (result != 1) {
//                throw new GlobalException(CodeMsg.UPDATE_ERROR);
//            }
        }
        TimeLimit timeLimit = new TimeLimit();
        BeanUtils.copyProperties(form,timeLimit);
        timeLimit.setLimitCollege(form.getCollege());
        timeLimit.setTimeLimitType(TimeLimitType.SECONDARY_UNIT_REPORT_LIMIT.getValue());
        int result = timeLimitMapper.update(timeLimit);
        if (result != 1){
            throw new GlobalException(CodeMsg.UPDATE_ERROR);
        }
        return Result.success();
    }

    @Override
    public Result setApplyLimitAmount(ProjectApplyAmountLimitForm form) {
        AmountLimit amountLimit = new AmountLimit();
        BeanUtils.copyProperties(form,amountLimit);
        amountLimit.setLimitUnit(RoleType.MENTOR.getValue());
        amountLimitMapper.insertOne(amountLimit);
        return Result.success();
    }

    @Override
    public Result updateApplyLimitAmount(ProjectApplyAmountLimitForm form) {
        int result = amountLimitMapper.updateTimeLimit(form.getId(),form.getMaxAmount());
        if (result != 0) {
            throw new GlobalException(CodeMsg.UPDATE_ERROR);
        }
        return Result.success();
    }

    @Override
    public Result getProjectApplyAmountLimit() {
        List<AmountLimitVO> limitVOS = amountLimitMapper.getAmountLimitVOListByCollegeAndProjectType(null,null,RoleType.MENTOR.getValue());
        return Result.success(limitVOS);
    }
}

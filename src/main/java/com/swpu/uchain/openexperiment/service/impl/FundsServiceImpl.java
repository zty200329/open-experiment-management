package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.mapper.FundsMapper;
import com.swpu.uchain.openexperiment.mapper.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.FundsStatus;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.funds.FundForm;
import com.swpu.uchain.openexperiment.form.funds.FundsForm;
import com.swpu.uchain.openexperiment.form.funds.FundsUpdateForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.FundsKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.FundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 实现资金管理模块
 */
@Service
public class FundsServiceImpl implements FundsService {
    @Autowired
    private FundsMapper fundsMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProjectGroupMapper projectGroupMapper;

    @Override
    public boolean insert(Funds funds) {
        if (fundsMapper.insert(funds) == 1){
            redisService.delete(FundsKey.getByProjectGroupId, funds.getProjectGroupId() + "");
            redisService.set(FundsKey.getById, funds.getId() + "", funds);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Funds funds) {
        if (fundsMapper.updateByPrimaryKey(funds) == 1){
            redisService.set(FundsKey.getById, funds.getId() + "", funds);
            redisService.delete(FundsKey.getByProjectGroupId, funds.getProjectGroupId() + "");
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        Funds funds = selectById(id);
        if (funds == null){
            return;
        }
        redisService.delete(FundsKey.getByProjectGroupId, funds.getProjectGroupId() + "");
        redisService.delete(FundsKey.getById, id + "");
        fundsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Funds selectById(Long id) {
        Funds funds = redisService.get(FundsKey.getById, id + "", Funds.class);
        if (funds == null){
            funds = fundsMapper.selectByPrimaryKey(id);
            if (funds != null){
                redisService.set(FundsKey.getById, id + "", funds);
            }
        }
        return funds;
    }

    @Override
    public List<Funds> getFundsDetails(Long projectGroupId) {
        return fundsMapper.selectByProjectGroupId(projectGroupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result agreeFunds(Long projectGroupId) {
        if (fundsMapper.updateProjectFundsStatus(projectGroupId, FundsStatus.AGREED.getValue()) != 0){
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    /**
     *  资金报销
     * @param form
     * @return
     */
    @Override
    public Result cashReimbursement(FundsForm form) {
        List<Funds> list = new ArrayList<>();
        for (FundForm funds:form.getFundFormList()
             ) {
            Funds funds1 = new Funds();
            funds1.setAmount(funds.getAmount());
            funds1.setCreateTime(new Date());
            funds1.setProjectGroupId(form.getProjectId());
            funds1.setType(funds.getType());
            funds1.setUpdateTime(new Date());
            funds1.setUse(funds.getUse());
            list.add(funds1);
        }
        int result = fundsMapper.multiInsert(list);
        if (result != list.size()){
            throw new GlobalException(CodeMsg.FUNDS_APPLY_ERROR);
        }
        return Result.success();
    }


    @Override
    public Result updateProjectApplyFundsBySecondaryUnit(FundsUpdateForm form) {
        if (form.getProjectIdList().size() == 0){
            throw new GlobalException(CodeMsg.UPDATE_ERROR);
        }
        fundsMapper.updateProjectListFunds(form.getProjectIdList(),form.getFundsMount());
        return Result.success();
    }

}

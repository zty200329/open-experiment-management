package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.FundsMapper;
import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.FundsStatus;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.FundsKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.FundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Funds> fundsList = redisService.getArraylist(FundsKey.getByProjectGroupId, projectGroupId + "", Funds.class);
        if (fundsList == null || fundsList.size() == 0){
            fundsList = fundsMapper.selectByProjectGroupId(projectGroupId);
            if (fundsList != null && fundsList.size() != 0){
                redisService.set(FundsKey.getByProjectGroupId, projectGroupId + "", fundsList);
            }
        }
        return fundsList;
    }

    @Override
    @Transactional
    public Result agreeFunds(Long projectGroupId) {
        if (fundsMapper.updateProjectFundsStatus(projectGroupId, FundsStatus.AGREED.getValue()) != 0){
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

}

package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.FundsMapper;
import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.FundsKey;
import com.swpu.uchain.openexperiment.service.FundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return false;
    }

    @Override
    public boolean update(Funds funds) {
        return false;
    }

    @Override
    public void delete(Long id) {

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

}

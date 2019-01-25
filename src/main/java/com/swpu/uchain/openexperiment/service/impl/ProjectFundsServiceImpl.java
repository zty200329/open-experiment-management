package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.ProjectGroupFundsMapper;
import com.swpu.uchain.openexperiment.domain.ProjectGroupFunds;
import com.swpu.uchain.openexperiment.service.ProjectFundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 */
@Service
public class ProjectFundsServiceImpl implements ProjectFundsService {
    @Autowired
    private ProjectGroupFundsMapper projectFundsMapper;
    @Override
    public boolean insert(ProjectGroupFunds projectGroupFunds) {
        return false;
    }

    @Override
    public boolean update(ProjectGroupFunds projectGroupFunds) {
        return false;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ProjectGroupFunds selectProjectIdAndFundsId(Long projectGroupId, Long fundsId) {
        return projectFundsMapper.selectByProjectIdAndFundsId(projectGroupId, fundsId);
    }
}

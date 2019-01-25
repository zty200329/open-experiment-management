package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.ProjectGroupFunds;


/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 项目资金关系模块
 */
public interface ProjectFundsService {
    /**
     * 新增项目资金关系
     * @param projectGroupFunds
     * @return
     */
    boolean insert(ProjectGroupFunds projectGroupFunds);

    /**
     * 更新项目资金关系
     * @param projectGroupFunds
     * @return
     */
    boolean update(ProjectGroupFunds projectGroupFunds);

    /**
     * 删除项目资金关系
     * @param id
     */
    void delete(Long id);


    /**
     * 搜索项目的某一笔金额的关系
     * @param projectGroupId
     * @param fundsId
     * @return
     */
    ProjectGroupFunds selectProjectIdAndFundsId(Long projectGroupId, Long fundsId);
}

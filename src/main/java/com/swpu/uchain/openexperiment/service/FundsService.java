package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.result.Result;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 */
public interface FundsService {
    /**
     * 新增经费
     * @param funds
     * @return
     */
    boolean insert(Funds funds);

    /**
     * 更新经费信息
     * @param funds
     * @return
     */
    boolean update(Funds funds);

    /**
     * 删除经费信息
     * @param id
     */
    void delete(Long id);

    /**
     * 按照id进行查找
     * @param id
     * @return
     */
    Funds selectById(Long id);


    /**
     * 获取某项目的所有经费详情
     * @param projectGroupId
     * @return
     */
    List<Funds> getFundsDetails(Long projectGroupId);

    /**
     * 批准某项目立项资金
     * @param projectGroupId
     * @return
     */
    Result agreeFunds(Long projectGroupId);
}

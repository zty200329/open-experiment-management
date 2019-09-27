package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.result.Result;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FundsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Funds record);

    Funds selectByPrimaryKey(Long id);

    List<Funds> selectAll();

    int updateByPrimaryKey(Funds record);

    List<Funds> selectByProjectGroupId(Long projectGroupId);

    int updateProjectFundsStatus(Long projectGroupId, Integer fundsStatus);
}
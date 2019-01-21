package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.PolemicGroup;
import java.util.List;

public interface PolemicGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PolemicGroup record);

    PolemicGroup selectByPrimaryKey(Long id);

    List<PolemicGroup> selectAll();

    int updateByPrimaryKey(PolemicGroup record);
}
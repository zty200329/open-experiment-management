package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.PolemicGroup;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PolemicGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PolemicGroup record);

    PolemicGroup selectByPrimaryKey(Long id);

    List<PolemicGroup> selectAll();

    int updateByPrimaryKey(PolemicGroup record);
}
package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.HitBackMessage;
import java.util.List;

public interface HitBackMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HitBackMessage record);

    HitBackMessage selectByPrimaryKey(Long id);

    List<HitBackMessage> selectAll();

    int updateByPrimaryKey(HitBackMessage record);
}
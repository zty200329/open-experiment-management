package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.ReceiveNotice;
import java.util.List;

public interface ReceiveNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReceiveNotice record);

    ReceiveNotice selectByPrimaryKey(Long id);

    List<ReceiveNotice> selectAll();

    int updateByPrimaryKey(ReceiveNotice record);
}
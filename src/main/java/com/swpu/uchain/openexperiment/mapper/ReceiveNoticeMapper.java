package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.ReceiveNotice;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReceiveNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReceiveNotice record);

    ReceiveNotice selectByPrimaryKey(Long id);

    List<ReceiveNotice> selectAll();

    int updateByPrimaryKey(ReceiveNotice record);
}
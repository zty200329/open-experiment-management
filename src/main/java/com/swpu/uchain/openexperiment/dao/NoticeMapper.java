package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.Notice;
import java.util.List;

public interface NoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Notice record);

    Notice selectByPrimaryKey(Long id);

    List<Notice> selectAll();

    int updateByPrimaryKey(Notice record);
}
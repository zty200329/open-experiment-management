package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.HitBackMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Repository
public interface HitBackMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HitBackMessage record);

    HitBackMessage selectByPrimaryKey(Long id);

    List<HitBackMessage> selectAll();

    List<HitBackMessage> selectByUserIdAndNotRead( Long userId);

    int updateByPrimaryKey(HitBackMessage record);
}
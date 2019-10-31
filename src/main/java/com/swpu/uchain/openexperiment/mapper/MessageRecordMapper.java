package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author panghu
 */
@Repository
public interface MessageRecordMapper {

    int insert(Message record);

    int deleteById(@Param("id")Long id);

    int deleteByUserId(@Param("userId")Long userId);

}

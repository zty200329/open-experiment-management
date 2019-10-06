package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.domain.Message;
import org.springframework.stereotype.Repository;

/**
 * @author panghu
 */
@Repository
public interface MessageRecordMapper {

    int insert(Message record);

}

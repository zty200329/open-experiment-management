package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.DTO.OperationRecordDTO;
import com.swpu.uchain.openexperiment.domain.Announcement;
import com.swpu.uchain.openexperiment.domain.OperationRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author panghu
 */
@Repository
public interface OperationRecordMapper {

    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条数据
     * @param record 待插入的数据
     * @return
     */
    int insert(OperationRecordDTO record);

    int multiInsert(List<OperationRecordDTO> list);

    Announcement selectByPrimaryKey(Long id);

    List<Announcement> selectAll();

    int updateByPrimaryKey(Announcement record);

}

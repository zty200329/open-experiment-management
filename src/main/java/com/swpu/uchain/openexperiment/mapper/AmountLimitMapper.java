package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.VO.limit.AmountLimitVO;
import com.swpu.uchain.openexperiment.domain.AmountLimit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dengg
 */
@Repository
public interface AmountLimitMapper {

    int multiInsert(List<AmountLimit> list);

    int insertOne(AmountLimit amountLimit);

    List<AmountLimitVO> getAmountLimitVOByCollegeAndProjectType(@Param("college") Integer college, @Param("type") Integer projectType);

    int updateTimeLimit(@Param("id") Integer id,@Param("maxAmount") Integer maxAmount);

    int deleteLimit(@Param("id") Integer id);

}

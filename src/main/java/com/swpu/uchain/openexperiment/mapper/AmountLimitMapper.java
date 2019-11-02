package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.VO.TimeLimitVO;
import com.swpu.uchain.openexperiment.domain.AmountLimit;
import org.apache.ibatis.annotations.Param;

/**
 * @author dengg
 */
public interface AmountLimitMapper {

    int insertOne(AmountLimit amountLimit);

    TimeLimitVO getTimeLimitVOByCollegeAndUnit(Integer college,Integer unit);

    int updateTimeLimit(@Param("id") Integer id,@Param("maxAmount") Integer maxAmount,@Param("minAmount") Integer minAmount);

    int deleteLimit(@Param("id") Integer id);

}
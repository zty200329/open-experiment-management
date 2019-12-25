package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.TimeLimit;
import com.swpu.uchain.openexperiment.form.time.TimeLimitForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dengg
 */
@Repository
public interface TimeLimitMapper {

    int multiInsert(List<TimeLimit> list);

    int update(TimeLimit timeLimit);

    int delete(Integer type);

    TimeLimit getTimeLimitByTypeAndCollege(@Param("type") Integer type,@Param("college") Integer college);

    List<TimeLimit> getAllByCollege(@Param("college") Integer college);
}

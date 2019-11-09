package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.TimeLimit;
import com.swpu.uchain.openexperiment.form.time.TimeLimitForm;
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

    TimeLimit getTimeLimitById(Integer type);

}

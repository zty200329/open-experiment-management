package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.TimeLimit;
import com.swpu.uchain.openexperiment.form.time.TimeLimitForm;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeLimitMapper {

    int insert(TimeLimitForm form);

    int update(TimeLimitForm form);

    int delete(Integer type);

    TimeLimit getTimeLimitById(Integer type);

}

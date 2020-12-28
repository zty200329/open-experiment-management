package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.TimeLimit;
import com.swpu.uchain.openexperiment.enums.TimeLimitType;
import com.swpu.uchain.openexperiment.form.time.TimeLimitForm;
import com.swpu.uchain.openexperiment.result.Result;

public interface TimeLimitService {

    Result insert(TimeLimitForm form);

    Result update(TimeLimitForm form);

    Result delete(Integer type);

    Result getTimeLimitList();

    TimeLimit getTimeLimitByTypeAndCollege(TimeLimitType timeLimitType,Integer college);

    void validTime(TimeLimitType timeLimitType);
    void validTime(TimeLimitType timeLimitType, Integer college);

}

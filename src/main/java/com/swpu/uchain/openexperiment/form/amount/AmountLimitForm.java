package com.swpu.uchain.openexperiment.form.amount;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author: panghu
 * @Date:
 */
@Data
public class AmountLimitForm {

    @NotNull
    private Integer limitCollege;

    private Date startTime;

    private Date endTime;

    @NotNull
    List<AmountAndType> list;


}

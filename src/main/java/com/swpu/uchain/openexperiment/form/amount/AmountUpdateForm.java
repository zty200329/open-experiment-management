package com.swpu.uchain.openexperiment.form.amount;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author dengg
 */
@Data
public class AmountUpdateForm {

    private Integer college;

    private Date startTime;

    private Date endTime;

    List<AmountAndType> list;

}

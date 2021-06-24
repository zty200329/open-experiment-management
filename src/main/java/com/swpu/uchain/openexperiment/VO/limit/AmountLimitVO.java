package com.swpu.uchain.openexperiment.VO.limit;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author dengg
 */
@Data
public class AmountLimitVO {

    private Integer college;

    private Date startTime;

    private Date endTime;

    List<AmountAndTypeVO> list;

}

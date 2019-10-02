package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author panghu
 */
@Data
public class Funds implements Serializable {

    private Long id;

    private Integer amount;

    private String use;

    private Integer type;

    private Long applicantId;

    private Long projectGroupId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
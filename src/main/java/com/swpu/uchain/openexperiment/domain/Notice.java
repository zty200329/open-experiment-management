package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author panghu
 */
@Data
public class Notice implements Serializable {

    private Long id;

    private String title;

    private Long senderId;

    private String message;

    private Date sendTime;

    private static final long serialVersionUID = 1L;


}
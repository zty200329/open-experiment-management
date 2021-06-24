package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Role implements Serializable {

    private Long id;

    private Date createTime;

    private String name;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author panghu
 */
@Data
public class Announcement{

    private Long id;

    private Long publisherId;

    private String title;

    private String content;

    private Date publishTime;

    private Date updateTime;

    private Integer status;

    private Integer college;

}
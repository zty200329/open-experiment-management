package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * zty
 * @author Administrator
 */
@Data
public class ProjectReviewResult implements Serializable {
    private Integer id;

    private Long projectId;

    private Integer score;

    private Boolean isSupport;

    private String reason;

    private Long operateUser;

    private static final long serialVersionUID = 1L;


}
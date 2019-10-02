package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author panghu
 */
@Data
public class UserProjectGroup implements Serializable {

    private Long id;

    private Long projectGroupId;

    private Long userId;

    private String technicalRole;

    private String workDivision;

    private Integer status;

    private Integer memberRole;

    private String personalJudge;

    private Date updateTime;

    private Date joinTime;

    private static final long serialVersionUID = 1L;

}
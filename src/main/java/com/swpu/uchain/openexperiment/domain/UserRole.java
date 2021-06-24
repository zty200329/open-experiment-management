package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author panghu
 */
@Data
public class UserRole implements Serializable {

    private Long id;

    private Long userId;

    private Integer roleId;

    private static final long serialVersionUID = 1L;

}
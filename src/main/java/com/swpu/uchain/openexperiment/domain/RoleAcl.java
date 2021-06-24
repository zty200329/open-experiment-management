package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author panghu
 */
@Data
public class RoleAcl implements Serializable {

    private Long id;

    private Long roleId;

    private Long aclId;

    private static final long serialVersionUID = 1L;


}
package com.swpu.uchain.openexperiment.VO.permission;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: clf
 * @Date: 19-3-12
 * @Description:
 */
@Data
public class RoleVO implements Serializable {
    private Long id;
    private String roleName;
}

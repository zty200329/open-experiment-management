package com.swpu.uchain.openexperiment.VO.user;

import lombok.Data;

/**
 * @author dengg
 */
@Data
public class RoleUserVO {
    private Long id;
    private String realName;
    private String code;
    private String mobilePhone;
    private String email;
    private String workUnit;
    private Integer role;
}

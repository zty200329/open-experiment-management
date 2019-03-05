package com.swpu.uchain.openexperiment.VO.user;

import lombok.Data;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-3-5
 * @Description:
 */
@Data
public class UserManageInfo {
    private Long id;
    private String realName;
    private String userCode;
    private String mobilePhone;
    private String email;
    private List<String> roles;
}

package com.swpu.uchain.openexperiment.VO.user;

import com.swpu.uchain.openexperiment.VO.permission.RoleVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-3-5
 * @Description:
 */
@Data
public class UserManageInfo implements Serializable {
    private Long id;
    private String realName;
    private String code;
    private String mobilePhone;
    private String email;
    private List<RoleVO> roles;
}

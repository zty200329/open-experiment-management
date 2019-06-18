package com.swpu.uchain.openexperiment.VO.user;

import com.swpu.uchain.openexperiment.VO.permission.RoleInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-29
 * @Description:
 */
@Data
public class UserInfoVO implements Serializable {
    private Long id;

    private String code;

    private String email;

    private String fixPhone;

    private String idCard;

    private String mobilePhone;

    private String qqNum;

    private String realName;

    private String sex;

    private Integer userType;

    private String institute;

    private String major;

    private Integer grade;

    private String workUnit;

    private Integer classNum;

    private List<RoleInfoVO> roleInfoVOS;
}

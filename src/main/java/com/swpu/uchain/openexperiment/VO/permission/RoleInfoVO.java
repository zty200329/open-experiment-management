package com.swpu.uchain.openexperiment.VO.permission;

import com.swpu.uchain.openexperiment.domain.Acl;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-3-6
 * @Description:
 */
@Data
public class RoleInfoVO implements Serializable {
    private Long id;
    private String name;
    private List<Acl> acls;
}

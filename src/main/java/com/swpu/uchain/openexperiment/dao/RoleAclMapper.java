package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.RoleAcl;
import java.util.List;

public interface RoleAclMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoleAcl record);

    RoleAcl selectByPrimaryKey(Long id);

    List<RoleAcl> selectAll();

    int updateByPrimaryKey(RoleAcl record);
}
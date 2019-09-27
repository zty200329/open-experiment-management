package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.RoleAcl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAclMapper {
    int deleteRoleIdAndAclId(Long roleId, Long aclId);

    int insert(RoleAcl record);

    RoleAcl selectByPrimaryKey(Long id);

    List<RoleAcl> selectAll();

    int updateByPrimaryKey(RoleAcl record);

    RoleAcl selectByRoleIdAndAclId(Long roleId, Long aclId);
}
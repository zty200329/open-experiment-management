package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.Acl;
import java.util.List;

public interface AclMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Acl record);

    Acl selectByPrimaryKey(Long id);

    List<Acl> selectAll();

    int updateByPrimaryKey(Acl record);

    List<Acl> selectByUserId(Long userId);

    Acl selectByName(String name);
}
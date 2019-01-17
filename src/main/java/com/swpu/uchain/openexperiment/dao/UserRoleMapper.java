package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.UserRole;
import java.util.List;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserRole record);

    UserRole selectByPrimaryKey(Long id);

    List<UserRole> selectAll();

    int updateByPrimaryKey(UserRole record);
}
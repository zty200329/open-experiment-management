package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {
    int deleteByUserIdAndRoleId(Long userId, Long roleId);

    int insert(UserRole record);

    UserRole selectByPrimaryKey(Long id);

    List<UserRole> selectAll();

    int updateByPrimaryKey(UserRole record);

    UserRole selectByUserIdAndRoleId(Long userId, Long roleId);

    List<UserRole> selectByRoleId(Long roleId);
}
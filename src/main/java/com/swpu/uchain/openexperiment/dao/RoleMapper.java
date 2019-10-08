package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.VO.permission.RoleVO;
import com.swpu.uchain.openexperiment.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    Role selectByRoleName(String roleName);

    List<RoleVO> getRoles(Long userId);

    Role selectByUserId(Long userId);
}
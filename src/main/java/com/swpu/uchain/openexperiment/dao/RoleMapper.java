package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.VO.permission.RoleVO;
import com.swpu.uchain.openexperiment.domain.Role;
import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    Role selectByRoleName(String roleName);

    List<RoleVO> getRoles(Long userId);

    List<Role> selectByUserId(Long userId);
}
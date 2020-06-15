package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.VO.permission.RoleVO;
import com.swpu.uchain.openexperiment.domain.Role;
import org.apache.ibatis.annotations.Param;
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

    Role getUserRoles(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
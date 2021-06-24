package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.VO.user.RoleUserVO;
import com.swpu.uchain.openexperiment.domain.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {

    int deleteByUserIdAndRoleId(Long userId, Long roleId);

    int insert(UserRole record);

    UserRole selectByPrimaryKey(Long id);

    /**
     * 通过工号获取角色信息
     * @param userId 工号
     * @return
     */
    List<UserRole> selectByUserId(@Param("userId") Long userId);

    /**
     * 用户用户角色集合
     * @param userId
     * @return
     */
    List<Integer> selectUserRolesById(@Param("userId") Long userId);

    List<UserRole> selectAll();

    int updateByPrimaryKey(UserRole record);

    int updateUserRoleByUserIdAndRole(@Param("userId") Long userId,@Param("role") Integer role);

    UserRole selectByUserIdAndRoleId(Long userId, Integer roleId);

    List<UserRole> selectByRoleId(Long roleId);


    List<RoleUserVO> getUserInfoByRole(@Param("role") Integer value);

    List<RoleUserVO> getCollegeUserInfoByCollege(@Param("college")Integer college);
}
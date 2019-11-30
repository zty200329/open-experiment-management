package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.mapper.UserMapper;
import com.swpu.uchain.openexperiment.mapper.UserRoleMapper;
import com.swpu.uchain.openexperiment.domain.UserRole;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.permission.UserRoleForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 实现用户角色管理模块
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public boolean insert(UserRole userRole) {
        return userRoleMapper.insert(userRole) == 1;
    }

    @Override
    public Result deleteByUserIdRoleId(Long userId, Integer roleId) {
        if (userRoleMapper.selectByUserId(userId) == null){
            throw new GlobalException(CodeMsg.USER_NO_EXIST);
        }
        int result = userRoleMapper.updateUserRoleByUserIdAndRole(userId, RoleType.MENTOR.getValue());
        if (result != 1){
            throw new GlobalException(CodeMsg.USER_INFORMATION_MATCH_ERROR);
        }
        return Result.success();
    }

    @Override
    public Result addUserRole(UserRoleForm userRoleForm) {
        if (userMapper.selectByUserCode(userRoleForm.getUserId().toString()) == null){
            throw new GlobalException(CodeMsg.USER_NO_EXIST);
        }
        UserRole userRole = userRoleMapper.selectByUserIdAndRoleId(userRoleForm.getUserId(), userRoleForm.getRoleId());

        if (userRole != null){
            return Result.error(CodeMsg.USER_ROLE_HAD_EXIST);
        }

        int result ;

        UserRole userRoleQueryResult = userRoleMapper.selectByUserId(userRoleForm.getUserId());

        //不存在则增加
        if (userRoleQueryResult == null) {
            UserRole userRoleDomain = new UserRole();
            userRoleDomain.setRoleId(userRoleForm.getRoleId());
            userRoleDomain.setUserId(userRoleForm.getUserId());
            result = userRoleMapper.insert(userRoleDomain);
        //存在则更新
        }else {
            if (userRoleQueryResult.getRoleId().equals(RoleType.NORMAL_STU.getValue()) ||
                userRoleQueryResult.getRoleId().equals(RoleType.PROJECT_LEADER.getValue())) {
                throw new GlobalException(CodeMsg.STUDENT_CANT_GAIN_THIS_PERMISSION);
            }
            result = userRoleMapper.updateUserRoleByUserIdAndRole(userRoleForm.getUserId(),userRoleForm.getRoleId());
        }
        if (result != 1) {
            return Result.error(CodeMsg.ADD_ERROR);
        }
        return Result.success();
    }

    @Override
    public List<UserRole> selectUsersByRoleId(Long roleId) {
        return userRoleMapper.selectByRoleId(roleId);
    }

    @Override
    public Result getUserInfoByRole() {
        return Result.success(userRoleMapper.getUserInfoByRole(RoleType.MENTOR.getValue()));
    }
}

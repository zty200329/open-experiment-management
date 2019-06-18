package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.UserProjectGroup;

import java.util.List;

public interface UserProjectGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserProjectGroup record);

    UserProjectGroup selectByPrimaryKey(Long id);

    List<UserProjectGroup> selectAll();

    int updateByPrimaryKey(UserProjectGroup record);

    List<UserProjectGroup> selectByProjectGroupId(Long projectGroupId);

    UserProjectGroup selectByProjectGroupIdAndUserId(Long projectGroupId, Long userId);

    void deleteByProjectGroupId(Long projectGroupId);

    List<UserProjectGroup> selectByProjectGroupIdAndJoinStatus(Long projectGroupId, Integer joinStatus);

    List<String> selectUserCodesByProjectGroupId(Long projectGroupId);
}
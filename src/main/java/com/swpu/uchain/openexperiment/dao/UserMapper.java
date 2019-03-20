package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User selectByUserCode(String userCode);

    List<User> selectProjectJoinedUsers(Long projectId);

    List<User> selectByRandom(@Param("keyWord") String keyWord, boolean isTeacher);

    User selectGroupLeader(Long projectGroupId);
}
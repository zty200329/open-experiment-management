package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User selectByUserCode(String userCode);

    User selectByUserCodeAndRole(@Param("userCode") String userCode,@Param("role") Integer role);

    List<User> selectProjectJoinedUsers(Long projectId);

    List<User> selectByRandom(@Param("keyWord") String keyWord,@Param("isTeacher") boolean isTeacher);

    User selectGroupLeader(Long projectGroupId);

}
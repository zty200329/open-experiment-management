package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.VO.user.UserInfoVO2;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.form.user.UpdateUserCollegeForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByCode(String id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    int updateCollegeByCode(UpdateUserCollegeForm updateUserCollegeForm);

    int updateProjectCollegeByCode(UpdateUserCollegeForm updateUserCollegeForm);

    User selectByUserCode(String userCode);

    UserInfoVO2 selectByUserCode1(String userCode);

    User selectByUserCodeAndRole(@Param("userCode") String userCode,@Param("role") Integer role);

    List<User> selectProjectJoinedUsers(Long projectId);

    /**
     * 调整数据库结构，水平拆分表，分离学生表和教师表
     * @param keyWord
     * @param isTeacher
     * @return
     */
    List<User> selectByRandom(@Param("keyWord") String keyWord,@Param("isTeacher") boolean isTeacher);

    /**
     * 根据角色进行筛选  @desperate
     * @param projectGroupId
     * @return
     */
    User selectGroupLeader(Long projectGroupId);

}
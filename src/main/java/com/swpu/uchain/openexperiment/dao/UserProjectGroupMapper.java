package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.UserProjectGroup;
import com.swpu.uchain.openexperiment.form.user.StuMember;
import com.swpu.uchain.openexperiment.form.user.TeacherMember;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author panghu
 */
@Repository
public interface UserProjectGroupMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserProjectGroup record);

    UserProjectGroup selectByPrimaryKey(Long id);

    List<UserProjectGroup> selectAllByUserId(@Param("userId")Long userId);

    int updateByPrimaryKey(UserProjectGroup record);

    List<UserProjectGroup> selectByProjectGroupId(Long projectGroupId);

    UserProjectGroup selectByProjectGroupIdAndUserId(Long projectGroupId, Long userId);

    void deleteByProjectGroupId(Long projectGroupId);

    List<UserProjectGroup> selectByProjectGroupIdAndJoinStatus(Long projectGroupId, Integer joinStatus);

    List<String> selectUserCodesByProjectGroupId(Long projectGroupId);

    List<UserMemberVO> selectUserMemberVOListByMemberRoleAndProjectId(@Param("memberRole")Integer memberRole,@Param("projectId")Long projectId);

    /**
     * 通过角工号和角色查询项目ID
     * @param userId
     * @param memberRole
     * @return
     */
    List<Long> selectProjectGroupIdByUserIdAndMemberRole(@Param("userId") Long userId,@Param("memberRole")Integer memberRole);

    /**
     * 更新用户信息
     * @param stuMember
     * @return
     */
    int updateUserInfo(@Param("stuMember")StuMember stuMember,@Param("date") Date date,Long projectId);

    int updateTeacherTechnicalRole(TeacherMember teacher,@Param("projectId") Long projectId);

    /**
     * 通过项目编号和成员状态查询成员数量
     * @param id
     * @param status 1，申请 ，2.加入  3 被拒绝的
     * @return
     */
    Integer selectStuCount(@Param("projectId") Long id,@Param("status") Integer status);

    Integer getMemberAmountOfProject(@Param("projectId")Long projectId,@Param("memberRole")Integer memberRole);
}
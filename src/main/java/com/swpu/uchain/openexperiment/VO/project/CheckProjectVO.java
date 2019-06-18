package com.swpu.uchain.openexperiment.VO.project;

import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-29
 * @Description:
 * 审批立项报告VO
 */
@Data
public class CheckProjectVO implements Serializable {
    private String limitCollege;
    private Long projectGroupId;
    private String projectName;
    private Integer experimentType;
    private Integer totalHours;
    private List<UserMemberVO> guidanceTeachers;
    private List<UserMemberVO> memberStudents;
    private Date startTime;
    private Date endTime;
    private String labName;
    private String address;
    private String groupLeaderPhone;
    private Integer fundsApplyAmount;
    private Integer suggestGroupType;
    private Integer projectType;
    private Long applyFileId;
}

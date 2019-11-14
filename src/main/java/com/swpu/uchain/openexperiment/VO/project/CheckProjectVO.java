package com.swpu.uchain.openexperiment.VO.project;

import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.enums.CollegeType;
import lombok.Data;

import javax.validation.constraints.Null;
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
public class CheckProjectVO{

    private String serialNumber;
    private String limitCollege;
    private Long id;
    private String projectName;
    private Integer experimentType;
    private Integer totalHours;
    private Date startTime;
    private Date endTime;
    private String labName;
    private String address;
    private String groupLeaderPhone;
    private Integer applyFunds;
    private Integer suggestGroupType;
    private Integer projectType;
    private Long applyFileId;
    //项目是否开放
    private Integer isOpenTopic;
    /**
     * 已选项目人数
     */
    private Integer numberOfTheSelected;

    /**
     * 所属学院  {@link CollegeType#getValue()}
     */
    private Integer subordinateCollege;

    private List<UserMemberVO> guidanceTeachers;
    private List<UserMemberVO> memberStudents;
}

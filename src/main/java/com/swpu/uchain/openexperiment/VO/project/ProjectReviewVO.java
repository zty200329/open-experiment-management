package com.swpu.uchain.openexperiment.VO.project;

import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.enums.CollegeType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author zty200329
 * @date 2020/10/7 19:45
 * @describe:
 */
@Data
public class ProjectReviewVO {
    private Long id;
    private String projectName;
    private Integer experimentType;
    private Date startTime;
    private Date endTime;
    private Integer applyFunds;
    private Integer suggestGroupType;
    private Integer projectType;

    private Integer score;

    private String reason;

    private Integer isSupport;

    /**
     * 已选项目人数
     */
    private Integer numberOfTheSelected;

    /**
     * 所属学院  {@link CollegeType#getValue()}
     */
    private Integer subordinateCollege;

    private List<UserMemberVO> guidanceTeachers;
}

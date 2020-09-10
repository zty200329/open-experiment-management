package com.swpu.uchain.openexperiment.VO.project;

import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.VO.user.UserVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zty200329
 * @date 2020/9/10 17:19
 * @describe:返回列表的VO
 */
@Data
public class NewCheckProjectVO {
    private Long id;
    private String projectName;
    private List<UserVO> guidanceTeachers;
    private Integer projectType;
    /**
     * 已选项目人数
     */
    private Integer numberOfTheSelected;
    private Integer experimentType;
    private Integer applyFunds;
    private Date startTime;
    private Date endTime;
}

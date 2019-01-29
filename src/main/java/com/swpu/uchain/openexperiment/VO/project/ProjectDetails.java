package com.swpu.uchain.openexperiment.VO.project;

import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.domain.ProjectFile;
import lombok.Data;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 */
@Data
public class ProjectDetails {
    /**
     * 项目组组长
     */
    private UserMemberVO leader;
    /**
     * 创建人
     */
    private UserMemberVO creator;
    /**
     * 实验室名称
     */
    private String labName;
    /**
     * 实验地点
     */
    private String address;
    /**
     * 参与成员
     */
    private List<UserMemberVO> members;
    /**
     * 申报经费总额
     */
    private Integer totalApplyFundsAmount;
    /**
     * 立项经费总额
     */
    private Integer totalAgreeFundsAmount;
    /**
     * 经费详情
     */
    private List<Funds> fundsDetails;
    /**
     * 项目相关文件
     */
    private List<ProjectFile> projectFiles;
}

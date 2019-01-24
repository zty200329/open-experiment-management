package com.swpu.uchain.openexperiment.VO;

import com.swpu.uchain.openexperiment.domain.Funds;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 * 重点项目立项申请表信息展示
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyPointFormInfoVO {
    /**
     * 项目组id
     */
    private Long projectGroupId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目类型
     */
    private String projectType;
    /**
     * 经费详情
     */
    private List<Funds> fundsDetails;
    /**
     * 项目文件id
     */
    private Long projectFileId;
    /**
     * 指导教师
     */
    private List<UserDetailVO> guideTeachers;
    /**
     * 学生成员
     */
    private List<UserDetailVO> stuMembers;

}

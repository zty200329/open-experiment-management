package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.domain.CollegeGivesGrade;
import com.swpu.uchain.openexperiment.domain.ProjectReview;
import com.swpu.uchain.openexperiment.domain.ProjectReviewResult;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.ProjectType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.project.CollegeGiveScore;
import com.swpu.uchain.openexperiment.form.reviews.NeedProjectReviewForm;
import com.swpu.uchain.openexperiment.mapper.CollegeGivesGradeMapper;
import com.swpu.uchain.openexperiment.mapper.ProjectReviewMapper;
import com.swpu.uchain.openexperiment.mapper.ProjectReviewResultMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.NeedProjectReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author zty200329
 * @date 2020/10/4 16:21
 * @describe:
 */
@Service
@Slf4j
public class NeedProjectReviewServiceImpl implements NeedProjectReviewService {
    @Autowired
    private GetUserService getUserService;
    @Autowired
    private ProjectReviewMapper projectReviewMapper;
    @Autowired
    private ProjectReviewResultMapper projectReviewResultMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result collegeSetUpReview(List<NeedProjectReviewForm> projectReviewForms) {
        User user = getUserService.getCurrentUser();
        if (user == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        List<ProjectReview> projectReviews = new LinkedList<>();
        for (NeedProjectReviewForm projectReviewForm : projectReviewForms) {
            //存在则跳过
            if(projectReviewMapper.selectByCollegeAndType(projectReviewForm.getCollege(),projectReviewForm.getProjectType()) != null){
                continue;
            }
            ProjectReview projectReview = new ProjectReview();
            BeanUtils.copyProperties(projectReviewForm,projectReview);
            //将原有的状态改变
            changeStateToReview(projectReviewForm);
            projectReviewMapper.insert(projectReview);
            projectReviews.add(projectReview);
        }
        return Result.success(projectReviews);
    }

    @Override
    public Result getCollegeReview() {
        return Result.success(projectReviewMapper.selectAll());
    }

    @Override
    public Result deleteCollegeReview(Integer id) {
        User user = getUserService.getCurrentUser();
        if (user == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        ProjectReview projectReview = projectReviewMapper.selectByPrimaryKey(id);
        changeStateToReview2(projectReview);
        projectReviewMapper.deleteByPrimaryKey(id);

        return Result.success();
    }


    /**
     * 改变状态
     * 将所有状态为待评审的全部改为待上报
     * @param projectReview
     */
    private void changeStateToReview2(ProjectReview projectReview){
        //如果是普通项目 直接就改一张表
        if(projectReview.getProjectType().equals(ProjectType.GENERAL.getValue())){
            projectReviewMapper.updateGeneralByCollegeAndType(projectReview.getCollege(),projectReview.getProjectType());
        }
        if(projectReview.getProjectType().equals(ProjectType.KEY.getValue())){
            projectReviewMapper.updateKeyByCollegeAndType(projectReview.getCollege());
        }
    }
    /**
     * 改变状态
     * 将所有状态为待上报的全部改为待评审
     * @param projectReviewForm
     */
    private void changeStateToReview(NeedProjectReviewForm projectReviewForm){
        //如果是普通项目 直接就改一张表
        if(projectReviewForm.getProjectType().equals(ProjectType.GENERAL.getValue())){
            projectReviewMapper.updateGeneralByCollegeAndType(projectReviewForm.getCollege(),projectReviewForm.getProjectType());
        }
        if(projectReviewForm.getProjectType().equals(ProjectType.KEY.getValue())){
            projectReviewMapper.updateKeyByCollegeAndType(projectReviewForm.getCollege());
        }
    }
}

package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.CollegeGivesGrade;
import com.swpu.uchain.openexperiment.form.project.CollegeGiveScore;
import com.swpu.uchain.openexperiment.form.reviews.NeedProjectReviewForm;
import com.swpu.uchain.openexperiment.result.Result;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zty200329
 * @date 2020/10/4 16:21
 * @describe:
 */
public interface NeedProjectReviewService {
    Result collegeSetUpReview(List<NeedProjectReviewForm> projectReviewForms);

    /**
     * 打分和推荐
     * @param collegeGivesGrade
     * @return
     */
    Result collegeSetScore(List<CollegeGiveScore> collegeGivesGrade);
}

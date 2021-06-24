package com.swpu.uchain.openexperiment.form.reviews;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zty200329
 * @date 2020/10/4 16:00
 * @describe:
 */
@Data
public class NeedProjectReviewForm {

    @NotNull(message = "学院不能为空")
    private Integer college;

    @NotNull(message = "项目类型")
    private Integer projectType;
}

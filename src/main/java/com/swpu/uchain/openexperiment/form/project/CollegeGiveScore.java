package com.swpu.uchain.openexperiment.form.project;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

/**
 * @author zty200329
 * @date 2020/10/6 20:51
 * @describe:
 */
@Data
public class CollegeGiveScore {
    @NotNull(message = "项目id不能为空")
    private Long projectId;

    @NotNull(message = "分数不能为空")
    private Integer score;

    @NotNull(message = "评审意见")
    private String reason;

    @NotNull(message = "是否推荐")
    private Integer isSupport;
}

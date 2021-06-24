package com.swpu.uchain.openexperiment.form.project;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zty200329
 * @date 2020/7/16 17:10
 * @describe:
 */
@Data
public class CollegeId {
    @NotNull(message = "id不能为空")
    private Integer id;
}

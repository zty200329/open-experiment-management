package com.swpu.uchain.openexperiment.form.announcement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author zty200329
 * @version 1.0
 * @date 2020/11/12 7:47 下午
 */
@Data
public class HomePageNewsPublishForm {
    @Length(max = 50, message = "字数不能超过50字")
    @NotNull(message = "标题不能为空")
    @ApiModelProperty("公告标题")
    private String title;

    @NotNull(message = "公告内容不能为空")
    @ApiModelProperty("公告内容,html字串")
    private String content;

    /**
     * 状态 1发布 0保存
     */
    @ApiModelProperty("状态 1发布 0保存")
    @NotNull(message = "发布状态")
    private Short status;
}

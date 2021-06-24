package com.swpu.uchain.openexperiment.form.announcement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author zty200329
 * @version 1.0
 * @date 2020/11/13 11:26 上午
 */
@Data
public class UpdateNewsContentForm {
    @NotNull(message = "id不能为空")
    private Integer id;

    @Length(max = 50, message = "字数不能超过50字")
    @NotNull(message = "标题不能为空")
    @ApiModelProperty("公告标题")
    private String title;

    @NotNull(message = "公告内容不能为空")
    @ApiModelProperty("公告内容,html字串")
    private String content;
}

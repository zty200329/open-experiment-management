package com.swpu.uchain.openexperiment.form.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author cby
 * @Date 19-1-25
 **/
@Data
public class SelectFileListForm {

    @ApiModelProperty("要查找文件对应的项目组Id")
    @NotNull(message = "项目组id不能为空")
    private Long groupId;
}

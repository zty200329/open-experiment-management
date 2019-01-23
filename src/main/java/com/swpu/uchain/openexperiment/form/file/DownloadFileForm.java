package com.swpu.uchain.openexperiment.form.file;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author cby
 * @Date 19-1-23
 * 下载文件 文件名
 **/
public class DownloadFileForm {

    @ApiModelProperty("要下载的文件名")
    @NotNull(message = "文件名不能为空")
    private String fileName;
}

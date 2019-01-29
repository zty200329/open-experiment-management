package com.swpu.uchain.openexperiment.form.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-29
 * @Description:
 * 结项报告表单
 */
@Data
public class ConcludingReportForm {
    @NotNull(message = "项目组id不能为空")
    @ApiModelProperty("项目组id")
    private Long projectGroupId;
    @NotNull(message = "文件不能为空")
    @ApiModelProperty("结题报告文件")
    private MultipartFile multipartFile;
}

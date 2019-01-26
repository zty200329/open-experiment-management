package com.swpu.uchain.openexperiment.form.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author cby
 * @Date 19-1-25
 **/
@Data
public class UploadFileForm {

    @ApiModelProperty("要上传的文件")
    @NotNull(message = "上传文件不能为空")
    private MultipartFile file;

    @ApiModelProperty("项目组Id")
    @NotNull(message = "项目组Id不能为空")
    private Long projectGroupId;

}

package com.swpu.uchain.openexperiment.form.permission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 接口权限的信息更新
 */
@Data
public class AclUpdateForm {
    @ApiModelProperty("权限id")
    @NotNull(message = "权限id不能为空")
    private Long aclId;
    @ApiModelProperty("描述信息")
    @Length(max = 100,message = "字数不能大于100")
    private String description;
}

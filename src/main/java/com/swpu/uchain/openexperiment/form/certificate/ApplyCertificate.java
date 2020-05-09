package com.swpu.uchain.openexperiment.form.certificate;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zty
 * @date 2020/5/9 下午9:07
 * @description:
 */
@Data
public class ApplyCertificate {
    /**
     * 项目编号
     */
    @NotNull(message = "编号不能为空")
    @ApiModelProperty("立项编号")
    private String serialNumber;

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    @ApiModelProperty("姓名")
    private String name;
    /**
     * 项目名称
     */
    @NotNull(message = "项目名称不能为空")
    @ApiModelProperty("项目名称")
    private String projectName;

    /**
     * 项目类型：1.普通 2.重点
     */
    @NotNull(message = "项目类别不能为空")
    @ApiModelProperty("项目类型：1.普通 2.重点")
    private Integer projectType;

    /**
     * 学号
     */
    @NotNull(message = "学号不能为空")
    @ApiModelProperty("学号")
    private Long userId;

    /**
     * 成员身份：1.指导教师2.项目组长3.普通成员
     */
    @NotNull(message = "成员身份不能为空")
    @ApiModelProperty("成员身份：2.项目组长3.普通成员")
    private Short memberRole;


    /**
     * 项目所属学院
     */
    @NotNull(message = "项目所属学院为空")
    @ApiModelProperty("项目所属学院")
    private String subordinateCollage;

}

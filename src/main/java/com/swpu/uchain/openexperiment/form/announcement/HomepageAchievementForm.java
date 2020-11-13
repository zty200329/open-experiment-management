package com.swpu.uchain.openexperiment.form.announcement;

import com.swpu.uchain.openexperiment.domain.NewsRelease;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zty200329
 * @version 1.0
 * @date 2020/11/14 12:17 上午
 */
@Data
public class HomepageAchievementForm {

    /**
     * 图片url
     */
    @NotNull(message = "图片不能为空")
    private String imgUrl;

    /**
     * 项目名称
     */
    @NotNull(message = "项目名不能为空")
    private String projectName;

    /**
     * 学院
     */
    @NotNull(message = "学院不能为空")
    private String subordinateCollege;

    /**
     * 项目类型 普通or重点
     */
    @NotNull(message = "项目类型不能为空")
    private Integer projectType;

    /**
     * 实验类型
     */
    @NotNull(message = "实验类型不能为空")
    private Integer experimentType;

    /**
     * 项目状态 1发布 2保存
     */
    @NotNull(message = "项目状态不能为空")
    private Integer status;

    /**
     * 开始时间
     */

    private Date startDate;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 文章内容
     */
    @NotNull(message = "内容可不能为空")
    private String content;
}

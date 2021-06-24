package com.swpu.uchain.openexperiment.form.project;

import lombok.Data;
import org.docx4j.wml.Id;

import javax.validation.constraints.NotNull;

/**
 * @author zty200329
 * @date 2020/6/13 21:12
 * @describe:
 */
@Data
public class ProjectGrade {

    @NotNull(message = "项目主键不能为空")
    private Long projectId;

    @NotNull(message = "评级不能为空")
    private Integer value;


}

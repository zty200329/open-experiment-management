package com.swpu.uchain.openexperiment.form.announcement;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zty200329
 * @version 1.0
 * @date 2020/11/13 10:50 上午
 */
@Data
public class IdForm {
    @NotNull(message = "id不能为空")
    Integer id;
}

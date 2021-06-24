package com.swpu.uchain.openexperiment.form.project;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zty
 */
@Data
public class GenericId {
    @NotNull(message = "id不能为空")
    private Long id;
}

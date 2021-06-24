package com.swpu.uchain.openexperiment.form.announcement;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 */

@Data
public class AnnouncementUpdateForm  {
    @NotNull(message = "公告id不能为空")
    private Long announcementId;

    private String title;

    private String content;
}
